package io.github.raphaelmun1z.ecommerce.services.autorizacao;

import io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao.EsquecerSenhaRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao.RedefinirSenhaRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao.SignUpRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.autorizacao.ClienteResponseDTO;
import io.github.raphaelmun1z.ecommerce.dtos.security.AccountCredentialsDTO;
import io.github.raphaelmun1z.ecommerce.dtos.security.TokenDTO;
import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Usuario;
import io.github.raphaelmun1z.ecommerce.exceptions.models.BadCredentialsException;
import io.github.raphaelmun1z.ecommerce.exceptions.models.BusinessException;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PapelRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.UsuarioRepository;
import io.github.raphaelmun1z.ecommerce.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PapelRepository papelRepository;

    public AuthService(
        AuthenticationManager authenticationManager,
        JwtTokenProvider tokenProvider,
        UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder,
        EmailService emailService,
        PapelRepository papelRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.papelRepository = papelRepository;
    }

    public TokenDTO signin(AccountCredentialsDTO credentials) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.senha());
            var auth = authenticationManager.authenticate(usernamePassword);
            var user = (Usuario) auth.getPrincipal();
            return tokenProvider.criarTokenDTO(user);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Email ou senha inválidos.");
        }
    }

    public TokenDTO refreshToken(String email, String refreshToken) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("E-mail " + email + " não encontrado."));
        return tokenProvider.atualizarToken(refreshToken, usuario);
    }

    @Transactional
    public ClienteResponseDTO signup(SignUpRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new BusinessException("O email informado já está em uso.");
        }

        Papel papelPadrao = papelRepository.findByNome("ROLE_CLIENTE")
            .orElseThrow(() -> new RuntimeException("Erro: Papel ROLE_CLIENTE não existe"));

        Cliente novoCliente = new Cliente(dto.nome(), dto.email(), passwordEncoder.encode(dto.senha()), papelPadrao, dto.cpf(), dto.telefone());
        Cliente clienteSalvo = usuarioRepository.save(novoCliente);
        return ClienteResponseDTO.fromEntity(clienteSalvo);
    }

    @Transactional
    public void esquecerSenha(EsquecerSenhaRequestDTO dto) {
        usuarioRepository.findByEmail(dto.email()).ifPresent(usuario -> {
            String token = UUID.randomUUID().toString();
            usuario.setPasswordResetToken(token);
            usuario.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));
            usuarioRepository.save(usuario);
            emailService.enviarEmailRedefinicaoSenha(usuario.getEmail(), token);
        });
    }

    @Transactional
    public void redefinirSenha(RedefinirSenhaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByPasswordResetToken(dto.token())
            .orElseThrow(() -> new BusinessException("Token de redefinição de senha inválido ou já utilizado."));

        if (usuario.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Token de redefinição de senha expirado.");
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuario.setPasswordResetToken(null);
        usuario.setPasswordResetTokenExpiry(null);
        usuarioRepository.save(usuario);
    }
}

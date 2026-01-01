package io.github.raphaelmun1z.ecommerce.controllers.autorizacao;

import io.github.raphaelmun1z.ecommerce.controllers.autorizacao.docs.AuthControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao.EsquecerSenhaRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao.RedefinirSenhaRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.req.autorizacao.SignUpRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.autorizacao.ClienteResponseDTO;
import io.github.raphaelmun1z.ecommerce.dtos.security.AccountCredentialsDTO;
import io.github.raphaelmun1z.ecommerce.dtos.security.TokenDTO;
import io.github.raphaelmun1z.ecommerce.services.autorizacao.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    @Override
    public ResponseEntity<TokenDTO> signin(@RequestBody @Valid AccountCredentialsDTO credentials) {
        return ResponseEntity.ok(authService.signin(credentials));
    }

    @PutMapping("/refresh/{email}")
    @Override
    public ResponseEntity<TokenDTO> refreshToken(
        @PathVariable("email") String email,
        @RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(email, refreshToken));
    }

    @PostMapping("/signup")
    @Override
    public ResponseEntity<ClienteResponseDTO> signup(@RequestBody @Valid SignUpRequestDTO dto) {
        ClienteResponseDTO novoCliente = authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @PostMapping("/forgot-password")
    @Override
    public ResponseEntity<Void> esquecerSenha(@RequestBody @Valid EsquecerSenhaRequestDTO dto) {
        authService.esquecerSenha(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @Override
    public ResponseEntity<Void> redefinirSenha(@RequestBody @Valid RedefinirSenhaRequestDTO dto) {
        authService.redefinirSenha(dto);
        return ResponseEntity.ok().build();
    }
}
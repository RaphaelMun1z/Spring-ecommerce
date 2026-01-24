package io.github.raphaelmun1z.ecommerce.controllers.usuario;

import io.github.raphaelmun1z.ecommerce.dtos.req.usuario.ClienteUpdateRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.usuario.UsuarioResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Usuario;
import io.github.raphaelmun1z.ecommerce.services.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listarTodos(
            @RequestParam(required = false, defaultValue = "") String termo,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<UsuarioResponseDTO> pagina = usuarioService.buscarPorTermo(termo, pageable);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> getMe(@AuthenticationPrincipal Usuario usuarioLogado) {
        Usuario usuario = usuarioService.buscarPorId(usuarioLogado.getId());
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }

    @PatchMapping("/me/avatar")
    public ResponseEntity<Void> atualizarAvatarMe(
        @AuthenticationPrincipal Usuario usuarioLogado,
        @RequestParam String fileName) {

        usuarioService.atualizarFotoPerfil(usuarioLogado.getId(), fileName);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> atualizarMeusDados(
        @AuthenticationPrincipal Usuario usuarioLogado,
        @RequestBody @Valid ClienteUpdateRequestDTO dto) {

        usuarioService.atualizarDadosCliente(usuarioLogado.getId(), dto);
        return ResponseEntity.noContent().build();
    }


    // --- ENDPOINTS LEGADOS (ADMIN) ---

    @PatchMapping("/{id}/avatar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> atualizarAvatarPorId(@PathVariable String id, @RequestParam String fileName) {
        usuarioService.atualizarFotoPerfil(id, fileName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable String id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }
}
package io.github.raphaelmun1z.ecommerce.controllers.operacoes;

import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.FavoritoResponseDTO;
import io.github.raphaelmun1z.ecommerce.services.operacoes.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/favoritos")
@Tag(name = "Favoritos", description = "Gestão da lista de desejos dos clientes")
public class FavoritoController {

    private final FavoritoService service;

    public FavoritoController(FavoritoService service) {
        this.service = service;
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar favoritos do cliente", description = "Retorna a lista paginada de produtos favoritados.")
    public ResponseEntity<Page<FavoritoResponseDTO>> listar(
        @PathVariable String clienteId,
        @PageableDefault(size = 20, sort = "dataAdicao") Pageable pageable) {

        Page<FavoritoResponseDTO> page = service.listarPorCliente(clienteId, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/cliente/{clienteId}/produto/{produtoId}")
    @Operation(summary = "Adicionar aos favoritos", description = "Marca um produto como favorito para o cliente especificado.")
    public ResponseEntity<FavoritoResponseDTO> adicionar(
        @PathVariable String clienteId,
        @PathVariable String produtoId) {

        FavoritoResponseDTO dto = service.adicionar(clienteId, produtoId);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(dto.getId())
            .toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @DeleteMapping("/cliente/{clienteId}/produto/{produtoId}")
    @Operation(summary = "Remover dos favoritos", description = "Remove um produto específico da lista de favoritos.")
    public ResponseEntity<Void> remover(
        @PathVariable String clienteId,
        @PathVariable String produtoId) {

        service.remover(clienteId, produtoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cliente/{clienteId}/limpar")
    @Operation(summary = "Limpar todos os favoritos", description = "Remove todos os itens da lista de desejos do cliente.")
    public ResponseEntity<Void> limparTudo(@PathVariable String clienteId) {
        service.limparTudo(clienteId);
        return ResponseEntity.noContent().build();
    }
}
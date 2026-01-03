package io.github.raphaelmun1z.ecommerce.controllers.catalogo;

import io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs.EstoqueControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.req.catalogo.MovimentacaoEstoqueRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.catalogo.MovimentacaoEstoqueResponseDTO;
import io.github.raphaelmun1z.ecommerce.services.catalogo.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/estoque")
public class EstoqueController implements EstoqueControllerDocs {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/historico/{produtoId}")
    public ResponseEntity<Page<MovimentacaoEstoqueResponseDTO>> buscarHistorico(@PathVariable String produtoId, Pageable pageable) {
        Page<MovimentacaoEstoqueResponseDTO> list = service.buscarHistoricoPorProduto(produtoId, pageable);
        return ResponseEntity.ok(list);
    }

    @Override
    @PostMapping("/movimentacao")
    public ResponseEntity<MovimentacaoEstoqueResponseDTO> registrarMovimentacao(@Valid @RequestBody MovimentacaoEstoqueRequestDTO dto) {
        MovimentacaoEstoqueResponseDTO newObj = service.registrarMovimentacaoManual(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(newObj);
    }
}
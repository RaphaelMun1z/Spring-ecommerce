package io.github.raphaelmun1z.ecommerce.controllers.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.estoque.MovimentacaoEstoque;
import io.github.raphaelmun1z.ecommerce.services.catalogo.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/estoque")
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @GetMapping(value = "/historico/{produtoId}")
    public ResponseEntity<Page<MovimentacaoEstoque>> findHistoricoByProduto(@PathVariable String produtoId, Pageable pageable) {
        Page<MovimentacaoEstoque> list = service.findHistoricoByProduto(produtoId, pageable);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping(value = "/movimentar")
    public ResponseEntity<MovimentacaoEstoque> registrarMovimentacao(@Valid @RequestBody MovimentacaoEstoque obj) {
        obj = service.registrarMovimentacao(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }
}
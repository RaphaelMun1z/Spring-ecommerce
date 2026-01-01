package io.github.raphaelmun1z.ecommerce.controllers.catalogo;

import io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs.ProdutoControllerDocs;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.services.catalogo.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoController implements ProdutoControllerDocs {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Produto>> findAll(Pageable pageable) {
        Page<Produto> list = service.findAll(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/vitrine")
    public ResponseEntity<Page<Produto>> findAllActive(Pageable pageable) {
        Page<Produto> list = service.findAllActive(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Produto> findById(@PathVariable String id) {
        Produto obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Produto> insert(@Valid @RequestBody Produto obj) {
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Produto> update(@PathVariable String id, @Valid @RequestBody Produto obj) {
        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable String id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable String id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }
}
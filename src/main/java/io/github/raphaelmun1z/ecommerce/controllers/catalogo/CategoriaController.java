package io.github.raphaelmun1z.ecommerce.controllers.catalogo;

import io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs.CategoriaControllerDocs;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Categoria;
import io.github.raphaelmun1z.ecommerce.services.catalogo.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaController implements CategoriaControllerDocs {
    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Categoria>> findAll(Pageable pageable) {
        Page<Categoria> list = service.findAll(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/ativas")
    public ResponseEntity<Page<Categoria>> findAllActive(Pageable pageable) {
        Page<Categoria> list = service.findAllActive(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Categoria> findById(@PathVariable String id) {
        Categoria obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Categoria> insert(@Valid @RequestBody Categoria obj) {
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Categoria> update(@PathVariable String id, @Valid @RequestBody Categoria obj) {
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
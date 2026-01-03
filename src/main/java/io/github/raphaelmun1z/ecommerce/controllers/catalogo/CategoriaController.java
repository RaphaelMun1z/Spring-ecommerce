package io.github.raphaelmun1z.ecommerce.controllers.catalogo;

import io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs.CategoriaControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.res.catalogo.CategoriaResponseDTO;
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

    @Override
    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDTO>> listarTodas(Pageable pageable) {
        Page<CategoriaResponseDTO> list = service.findAll(pageable);
        return ResponseEntity.ok().body(list);
    }

    @Override
    @GetMapping("/ativas")
    public ResponseEntity<Page<CategoriaResponseDTO>> listarAtivas(Pageable pageable) {
        Page<CategoriaResponseDTO> list = service.findAllActive(pageable);
        return ResponseEntity.ok().body(list);
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable String id) {
        CategoriaResponseDTO obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @Override
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody Categoria obj) {
        CategoriaResponseDTO newObj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(newObj);
    }

    @Override
    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable String id, @Valid @RequestBody Categoria obj) {
        CategoriaResponseDTO newObj = service.update(id, obj);
        return ResponseEntity.ok().body(newObj);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(value = "/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable String id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(value = "/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable String id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }
}
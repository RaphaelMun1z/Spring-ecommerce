package io.github.raphaelmun1z.ecommerce.controllers.catalogo;

import io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs.ProdutoControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.req.catalogo.ProdutoRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.catalogo.ProdutoResponseDTO;
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

    @Override
    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> listarTodos(Pageable pageable) {
        Page<ProdutoResponseDTO> list = service.listarTodos(pageable);
        return ResponseEntity.ok().body(list);
    }

    @Override
    @GetMapping("/vitrine")
    public ResponseEntity<Page<ProdutoResponseDTO>> listarAtivos(Pageable pageable) {
        Page<ProdutoResponseDTO> list = service.listarAtivos(pageable);
        return ResponseEntity.ok().body(list);
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable String id) {
        ProdutoResponseDTO obj = service.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @Override
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO newObj = service.criar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).body(newObj);
    }

    @Override
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable String id, @Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO obj = service.atualizar(id, dto);
        return ResponseEntity.ok().body(obj);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        service.excluir(id);
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
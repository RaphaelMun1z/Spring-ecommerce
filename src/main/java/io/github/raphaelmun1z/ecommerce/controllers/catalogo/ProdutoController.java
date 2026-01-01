package io.github.raphaelmun1z.ecommerce.controllers.catalogo;

import io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs.ProdutoControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.req.ProdutoRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.ProdutoResponseDTO;
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
    @Override
    public ResponseEntity<Page<ProdutoResponseDTO>> findAll(Pageable pageable) {
        Page<Produto> list = service.findAll(pageable);
        // Converte Page<Produto> para Page<ProdutoResponseDTO>
        Page<ProdutoResponseDTO> listDto = list.map(this::toDTO);
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/vitrine")
    @Override
    public ResponseEntity<Page<ProdutoResponseDTO>> findAllActive(Pageable pageable) {
        Page<Produto> list = service.findAllActive(pageable);
        return ResponseEntity.ok().body(list.map(this::toDTO));
    }

    @GetMapping(value = "/{id}")
    @Override
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable String id) {
        Produto obj = service.findById(id);
        return ResponseEntity.ok().body(toDTO(obj));
    }

    @PostMapping
    @Override
    public ResponseEntity<ProdutoResponseDTO> insert(@Valid @RequestBody ProdutoRequestDTO dto) {
        Produto obj = toEntity(dto);
        obj = service.insert(obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).body(toDTO(obj));
    }

    @PutMapping(value = "/{id}")
    @Override
    public ResponseEntity<ProdutoResponseDTO> update(@PathVariable String id, @Valid @RequestBody ProdutoRequestDTO dto) {
        Produto obj = toEntity(dto);
        obj = service.update(id, obj);
        return ResponseEntity.ok().body(toDTO(obj));
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/desativar")
    @Override
    public ResponseEntity<Void> desativar(@PathVariable String id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/ativar")
    @Override
    public ResponseEntity<Void> ativar(@PathVariable String id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }

    // --- Métodos de Conversão (Mappers manuais) ---
    private ProdutoResponseDTO toDTO(Produto entity) {
        return new ProdutoResponseDTO(entity);
    }

    private Produto toEntity(ProdutoRequestDTO dto) {
        Produto p = new Produto();
        p.setCodigoControle(dto.getCodigoControle());
        p.setTitulo(dto.getTitulo());
        p.setDescricao(dto.getDescricao());
        p.setPreco(dto.getPreco());
        p.setPrecoPromocional(dto.getPrecoPromocional());
        p.setEstoque(dto.getEstoque());
        p.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        p.setPesoKg(dto.getPesoKg());
        p.setDimensoes(dto.getDimensoes());
        // p.setCategoria(...) // Lógica para buscar categoria pelo ID se necessário
        return p;
    }
}
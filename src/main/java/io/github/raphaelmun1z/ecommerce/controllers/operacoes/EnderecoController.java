package io.github.raphaelmun1z.ecommerce.controllers.operacoes;

import io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs.EnderecoControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.req.usuario.EnderecoRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.usuario.EnderecoResponseDTO;
import io.github.raphaelmun1z.ecommerce.services.usuario.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController implements EnderecoControllerDocs {

    private final EnderecoService service;

    public EnderecoController(EnderecoService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<EnderecoResponseDTO>> listar(@PathVariable String clienteId) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @Override
    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<EnderecoResponseDTO> criar(@PathVariable String clienteId, @Valid @RequestBody EnderecoRequestDTO dto) {
        EnderecoResponseDTO obj = service.adicionar(clienteId, dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.id).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizar(@PathVariable String id, @Valid @RequestBody EnderecoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/principal")
    public ResponseEntity<Void> definirPrincipal(@PathVariable String id) {
        service.definirComoPrincipal(id);
        return ResponseEntity.noContent().build();
    }
}
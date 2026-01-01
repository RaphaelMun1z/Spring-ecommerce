package io.github.raphaelmun1z.ecommerce.controllers.operacoes;

import io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs.CarrinhoControllerDocs;
import io.github.raphaelmun1z.ecommerce.entities.carrinho.Carrinho;
import io.github.raphaelmun1z.ecommerce.services.operacoes.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController implements CarrinhoControllerDocs {
    private final CarrinhoService service;

    public CarrinhoController(CarrinhoService service) {
        this.service = service;
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Carrinho> buscarCarrinho(@PathVariable String clienteId) {
        Carrinho carrinho = service.buscarOuCriarCarrinho(clienteId);
        return ResponseEntity.ok(carrinho);
    }

    @PostMapping("/{clienteId}/adicionar")
    public ResponseEntity<Carrinho> adicionarItem(
        @PathVariable String clienteId,
        @RequestParam String produtoId,
        @RequestParam(defaultValue = "1") Integer quantidade) {

        Carrinho carrinho = service.adicionarItem(clienteId, produtoId, quantidade);
        return ResponseEntity.ok(carrinho);
    }

    @DeleteMapping("/{clienteId}/remover/{produtoId}")
    public ResponseEntity<Carrinho> removerItem(@PathVariable String clienteId, @PathVariable String produtoId) {
        Carrinho carrinho = service.removerItem(clienteId, produtoId);
        return ResponseEntity.ok(carrinho);
    }

    @PatchMapping("/{clienteId}/atualizar/{produtoId}")
    public ResponseEntity<Carrinho> atualizarQuantidade(
        @PathVariable String clienteId,
        @PathVariable String produtoId,
        @RequestParam Integer quantidade) {

        Carrinho carrinho = service.atualizarQuantidade(clienteId, produtoId, quantidade);
        return ResponseEntity.ok(carrinho);
    }

    @DeleteMapping("/{clienteId}/limpar")
    public ResponseEntity<Void> limparCarrinho(@PathVariable String clienteId) {
        service.limparCarrinho(clienteId);
        return ResponseEntity.noContent().build();
    }
}
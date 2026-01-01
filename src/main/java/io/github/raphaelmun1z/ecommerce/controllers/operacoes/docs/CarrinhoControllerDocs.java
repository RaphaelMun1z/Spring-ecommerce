package io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs;

import io.github.raphaelmun1z.ecommerce.entities.carrinho.Carrinho;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Carrinho de Compras", description = "Endpoints para gestão da sessão de compras (adicionar, remover e atualizar itens)")
public interface CarrinhoControllerDocs {

    @Operation(summary = "Buscar carrinho do cliente", description = "Retorna o carrinho ativo do cliente. Se não existir, cria um novo vazio.")
    @ApiResponse(responseCode = "200", description = "Carrinho recuperado com sucesso", content = @Content(schema = @Schema(implementation = Carrinho.class)))
    ResponseEntity<Carrinho> buscarCarrinho(
        @Parameter(description = "ID do cliente") @PathVariable String clienteId
    );

    @Operation(summary = "Adicionar item ao carrinho", description = "Adiciona um produto ao carrinho ou incrementa a quantidade se já existir.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Estoque insuficiente ou produto inativo", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cliente ou Produto não encontrado", content = @Content)
    })
    ResponseEntity<Carrinho> adicionarItem(
        @Parameter(description = "ID do cliente") @PathVariable String clienteId,
        @Parameter(description = "ID do produto") @RequestParam String produtoId,
        @Parameter(description = "Quantidade a adicionar (Padrão: 1)") @RequestParam(defaultValue = "1") Integer quantidade
    );

    @Operation(summary = "Remover item do carrinho", description = "Remove um produto específico do carrinho do cliente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Item não encontrado no carrinho", content = @Content)
    })
    ResponseEntity<Carrinho> removerItem(
        @Parameter(description = "ID do cliente") @PathVariable String clienteId,
        @Parameter(description = "ID do produto a remover") @PathVariable String produtoId
    );

    @Operation(summary = "Atualizar quantidade do item", description = "Define uma nova quantidade para um item já existente no carrinho.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quantidade atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Quantidade inválida ou estoque insuficiente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Item não encontrado no carrinho", content = @Content)
    })
    ResponseEntity<Carrinho> atualizarQuantidade(
        @Parameter(description = "ID do cliente") @PathVariable String clienteId,
        @Parameter(description = "ID do produto") @PathVariable String produtoId,
        @Parameter(description = "Nova quantidade absoluta") @RequestParam Integer quantidade
    );

    @Operation(summary = "Limpar carrinho", description = "Remove todos os itens do carrinho do cliente.")
    @ApiResponse(responseCode = "204", description = "Carrinho limpo com sucesso")
    ResponseEntity<Void> limparCarrinho(
        @Parameter(description = "ID do cliente") @PathVariable String clienteId
    );
}
package io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs;

import io.github.raphaelmun1z.ecommerce.entities.estoque.MovimentacaoEstoque;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Estoque", description = "Endpoints para controle de inventário e histórico de movimentações")
public interface EstoqueControllerDocs {

    @Operation(summary = "Consultar histórico do produto", description = "Retorna o histórico paginado de todas as entradas e saídas de um produto específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    ResponseEntity<Page<MovimentacaoEstoque>> findHistoricoByProduto(
        @Parameter(description = "ID do produto") @PathVariable String produtoId,
        @Parameter(hidden = true) Pageable pageable
    );

    @Operation(summary = "Registrar movimentação", description = "Registra uma nova entrada (compra/devolução) ou saída (venda/perda) no estoque.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Movimentação registrada e saldo atualizado", content = @Content(schema = @Schema(implementation = MovimentacaoEstoque.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou saldo insuficiente para saída", content = @Content),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    ResponseEntity<MovimentacaoEstoque> registrarMovimentacao(@RequestBody @Valid MovimentacaoEstoque obj);
}
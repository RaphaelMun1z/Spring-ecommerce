package io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPagamento;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pagamento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Pagamentos", description = "Endpoints para processamento, atualização e consulta de transações financeiras")
public interface PagamentoControllerDocs {

    @Operation(summary = "Buscar pagamento por ID", description = "Recupera os detalhes de um pagamento específico. O ID é o mesmo do Pedido associado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento encontrado", content = @Content(schema = @Schema(implementation = Pagamento.class))),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content)
    })
    ResponseEntity<Pagamento> findById(
        @Parameter(description = "ID do pagamento (mesmo ID do pedido)") @PathVariable String id
    );

    @Operation(summary = "Registrar pagamento", description = "Cria um registro de pagamento para um pedido aguardando pagamento.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pagamento registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação (valor incorreto, pedido já pago, etc.)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    ResponseEntity<Pagamento> create(
        @Parameter(description = "ID do pedido") @PathVariable String pedidoId,
        @RequestBody @Valid Pagamento pagamento
    );

    @Operation(summary = "Atualizar status do pagamento", description = "Endpoint para callbacks de gateway ou atualização manual do status financeiro.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Transição de status inválida", content = @Content),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado", content = @Content)
    })
    ResponseEntity<Pagamento> updateStatus(
        @Parameter(description = "ID do pagamento") @PathVariable String id,
        @Parameter(description = "Novo status") @RequestParam StatusPagamento status,
        @Parameter(description = "Código da transação no Gateway (Opcional)") @RequestParam(required = false) String codigoGateway
    );
}
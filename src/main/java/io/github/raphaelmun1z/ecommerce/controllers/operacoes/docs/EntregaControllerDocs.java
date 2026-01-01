package io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEntrega;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Entrega;
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

@Tag(name = "Entregas e Logística", description = "Endpoints para gestão de envios, rastreamento e status de entrega")
public interface EntregaControllerDocs {

    @Operation(summary = "Buscar entrega por ID", description = "Recupera os detalhes de uma entrega específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrega encontrada", content = @Content(schema = @Schema(implementation = Entrega.class))),
        @ApiResponse(responseCode = "404", description = "Entrega não encontrada", content = @Content)
    })
    ResponseEntity<Entrega> findById(
        @Parameter(description = "ID da entrega") @PathVariable String id
    );

    @Operation(summary = "Buscar entrega por Pedido", description = "Localiza o registro de entrega associado a um pedido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrega encontrada", content = @Content(schema = @Schema(implementation = Entrega.class))),
        @ApiResponse(responseCode = "404", description = "Entrega não encontrada para este pedido", content = @Content)
    })
    ResponseEntity<Entrega> findByPedido(
        @Parameter(description = "ID do pedido") @PathVariable String pedidoId
    );

    @Operation(summary = "Criar registro de entrega", description = "Gera o registro logístico inicial com snapshot do endereço. Geralmente chamado internamente ao finalizar pedido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Entrega criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Pedido já possui entrega vinculada", content = @Content),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    ResponseEntity<Entrega> create(
        @Parameter(description = "ID do pedido") @PathVariable String pedidoId,
        @RequestBody @Valid Entrega entrega
    );

    @Operation(summary = "Atualizar código de rastreio", description = "Define ou atualiza o código de rastreio e transportadora. Move status para ENVIADO se aplicável.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rastreio atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Não é possível alterar entrega finalizada", content = @Content),
        @ApiResponse(responseCode = "404", description = "Entrega não encontrada", content = @Content)
    })
    ResponseEntity<Entrega> updateTracking(
        @Parameter(description = "ID da entrega") @PathVariable String id,
        @Parameter(description = "Código de rastreio") @RequestParam String codigo,
        @Parameter(description = "Nome da transportadora") @RequestParam(required = false) String transportadora
    );

    @Operation(summary = "Atualizar status da entrega", description = "Altera manualmente o status logístico (ex: EM_SEPARACAO, ENVIADO).")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    ResponseEntity<Entrega> updateStatus(
        @Parameter(description = "ID da entrega") @PathVariable String id,
        @Parameter(description = "Novo status") @RequestParam StatusEntrega status
    );

    @Operation(summary = "Confirmar entrega realizada", description = "Marca a entrega como ENTREGUE e atualiza o pedido correspondente.")
    @ApiResponse(responseCode = "200", description = "Entrega confirmada com sucesso")
    ResponseEntity<Entrega> confirmDelivery(
        @Parameter(description = "ID da entrega") @PathVariable String id
    );
}
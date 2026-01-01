package io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Pedidos", description = "Endpoints para gestão de pedidos, checkout e histórico de compras")
public interface PedidoControllerDocs {

    @Operation(summary = "Buscar pedido por ID", description = "Recupera os detalhes completos de um pedido específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado", content = @Content(schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    ResponseEntity<Pedido> findById(
        @Parameter(description = "ID do pedido") @PathVariable String id
    );

    @Operation(summary = "Listar pedidos do cliente", description = "Retorna o histórico paginado de pedidos de um cliente específico.")
    @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso")
    ResponseEntity<Page<Pedido>> findByCliente(
        @Parameter(description = "ID do cliente") @PathVariable String clienteId,
        @Parameter(hidden = true) Pageable pageable
    );

    @Operation(summary = "Listar todos os pedidos (Admin)", description = "Endpoint administrativo para visualizar todos os pedidos da plataforma.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    ResponseEntity<Page<Pedido>> findAll(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Finalizar compra (Checkout)", description = "Transforma o carrinho atual do cliente em um novo pedido, calculando totais e baixando estoque.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso", content = @Content(schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação (estoque insuficiente, carrinho vazio, etc.)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cliente ou Carrinho não encontrado", content = @Content)
    })
    ResponseEntity<Pedido> createFromCart(
        @Parameter(description = "ID do cliente") @PathVariable String clienteId
    );

    @Operation(summary = "Atualizar status do pedido", description = "Altera o status do ciclo de vida do pedido (ex: ENVIADO, ENTREGUE).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Transição de status inválida", content = @Content),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    ResponseEntity<Pedido> updateStatus(
        @Parameter(description = "ID do pedido") @PathVariable String id,
        @Parameter(description = "Novo status") @RequestParam StatusPedido status
    );
}
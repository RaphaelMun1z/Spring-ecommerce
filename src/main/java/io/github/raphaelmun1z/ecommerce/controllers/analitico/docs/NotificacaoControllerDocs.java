package io.github.raphaelmun1z.ecommerce.controllers.analitico.docs;

import io.github.raphaelmun1z.ecommerce.dtos.res.analitico.NotificacaoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.Notificacao;
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

@Tag(name = "Notificações", description = "Endpoints para consulta de histórico de comunicações enviadas aos clientes (e-mail, SMS, etc)")
public interface NotificacaoControllerDocs {

    @Operation(summary = "Listar notificações do cliente", description = "Recupera o histórico paginado de notificações enviadas a um cliente específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso", content = @Content(schema = @Schema(implementation = NotificacaoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado (se validado)", content = @Content)
    })
    ResponseEntity<Page<NotificacaoResponseDTO>> listarPorCliente(
        @Parameter(description = "ID do cliente") @PathVariable String clienteId,
        @Parameter(hidden = true) Pageable pageable
    );
}
package io.github.raphaelmun1z.ecommerce.controllers.analitico.docs;

import io.github.raphaelmun1z.ecommerce.dtos.res.DashboardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Dashboard e Analytics", description = "Endpoints analíticos para o painel administrativo")
public interface DashboardControllerDocs {

    @Operation(summary = "Obter visão geral do dashboard", description = "Retorna os KPIs consolidados (Vendas, Pedidos, Ticket Médio), dados para gráficos e lista de pedidos recentes, comparando o mês atual com o anterior.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados do dashboard recuperados com sucesso", content = @Content(schema = @Schema(implementation = DashboardResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno ao processar métricas", content = @Content)
    })
    ResponseEntity<DashboardResponseDTO> getVisaoGeral();
}
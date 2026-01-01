package io.github.raphaelmun1z.ecommerce.controllers.analitico.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "Relatórios", description = "Endpoints para geração e exportação de relatórios gerenciais (Vendas, Estoque, etc.)")
public interface RelatorioControllerDocs {

    @Operation(summary = "Baixar relatório de vendas (CSV)", description = "Gera e faz o download de um arquivo CSV contendo o histórico de vendas no período especificado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arquivo gerado com sucesso",
            content = @Content(mediaType = "text/csv", schema = @Schema(type = "string", format = "binary"))),
        @ApiResponse(responseCode = "500", description = "Erro interno na geração do arquivo", content = @Content)
    })
    ResponseEntity<byte[]> baixarRelatorioVendas(
        @Parameter(description = "Data inicial (AAAA-MM-DD). Se omitido, considera o início do mês atual.")
        @RequestParam(required = false) LocalDate inicio,

        @Parameter(description = "Data final (AAAA-MM-DD). Se omitido, considera a data atual.")
        @RequestParam(required = false) LocalDate fim
    );

    @Operation(summary = "Baixar relatório de estoque (CSV)", description = "Gera e faz o download de um arquivo CSV com a posição atual de todos os produtos em estoque.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arquivo gerado com sucesso",
            content = @Content(mediaType = "text/csv", schema = @Schema(type = "string", format = "binary"))),
        @ApiResponse(responseCode = "500", description = "Erro interno na geração do arquivo", content = @Content)
    })
    ResponseEntity<byte[]> baixarRelatorioEstoque();
}
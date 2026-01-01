package io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs;

import io.github.raphaelmun1z.ecommerce.dtos.req.ProdutoRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.ProdutoResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
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

@Tag(name = "Catálogo de Produtos", description = "Endpoints para gerenciamento de produtos, inventário e vitrine")
public interface ProdutoControllerDocs {

    @Operation(summary = "Listar produtos (Administrativo)", description = "Retorna uma lista paginada de todos os produtos.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    ResponseEntity<Page<ProdutoResponseDTO>> findAll(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Vitrine de Produtos", description = "Retorna apenas produtos ativos e com estoque.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    ResponseEntity<Page<ProdutoResponseDTO>> findAllActive(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Buscar produto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado", content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    ResponseEntity<ProdutoResponseDTO> findById(@Parameter(description = "ID do produto") @PathVariable String id);

    @Operation(summary = "Cadastrar novo produto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso", content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou SKU duplicado", content = @Content)
    })
    ResponseEntity<ProdutoResponseDTO> insert(@RequestBody @Valid ProdutoRequestDTO dto);

    @Operation(summary = "Atualizar produto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    ResponseEntity<ProdutoResponseDTO> update(@Parameter(description = "ID do produto") @PathVariable String id, @RequestBody @Valid ProdutoRequestDTO dto);

    @Operation(summary = "Excluir produto (Físico)")
    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso")
    ResponseEntity<Void> delete(@Parameter(description = "ID do produto") @PathVariable String id);

    @Operation(summary = "Desativar produto (Lógico)")
    @ApiResponse(responseCode = "204", description = "Produto desativado com sucesso")
    ResponseEntity<Void> desativar(@Parameter(description = "ID do produto") @PathVariable String id);

    @Operation(summary = "Ativar produto")
    @ApiResponse(responseCode = "204", description = "Produto ativado com sucesso")
    ResponseEntity<Void> ativar(@Parameter(description = "ID do produto") @PathVariable String id);
}
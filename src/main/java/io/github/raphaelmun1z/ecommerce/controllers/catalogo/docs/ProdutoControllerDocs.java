package io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs;

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

    @Operation(summary = "Listar produtos (Administrativo)", description = "Retorna uma lista paginada de todos os produtos, incluindo inativos.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    ResponseEntity<Page<Produto>> findAll(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Vitrine de Produtos", description = "Retorna apenas produtos ativos e com estoque disponível para venda.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    ResponseEntity<Page<Produto>> findAllActive(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Buscar produto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado", content = @Content(schema = @Schema(implementation = Produto.class))),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    ResponseEntity<Produto> findById(@Parameter(description = "ID do produto") @PathVariable String id);

    @Operation(summary = "Cadastrar novo produto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou SKU duplicado", content = @Content)
    })
    ResponseEntity<Produto> insert(@RequestBody @Valid Produto obj);

    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    ResponseEntity<Produto> update(@Parameter(description = "ID do produto") @PathVariable String id, @RequestBody @Valid Produto obj);

    @Operation(summary = "Excluir produto (Físico)", description = "Remove o produto do banco de dados. Caso possua vínculos, retornará erro de integridade.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
        @ApiResponse(responseCode = "409", description = "Violação de integridade (produto já possui vendas)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    ResponseEntity<Void> delete(@Parameter(description = "ID do produto") @PathVariable String id);

    @Operation(summary = "Desativar produto", description = "Realiza a exclusão lógica (Soft Delete), removendo o produto da vitrine.")
    @ApiResponse(responseCode = "204", description = "Produto desativado com sucesso")
    ResponseEntity<Void> desativar(@Parameter(description = "ID do produto") @PathVariable String id);

    @Operation(summary = "Ativar produto", description = "Torna o produto visível novamente na vitrine.")
    @ApiResponse(responseCode = "204", description = "Produto ativado com sucesso")
    ResponseEntity<Void> ativar(@Parameter(description = "ID do produto") @PathVariable String id);
}
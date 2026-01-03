package io.github.raphaelmun1z.ecommerce.controllers.catalogo.docs;

import io.github.raphaelmun1z.ecommerce.dtos.res.catalogo.CategoriaResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Categoria;
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

@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias e subcategorias")
public interface CategoriaControllerDocs {

    @Operation(summary = "Listar todas as categorias (Administrativo)", description = "Retorna uma lista paginada de todas as categorias, incluindo inativas.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    ResponseEntity<Page<CategoriaResponseDTO>> listarTodas(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Listar categorias ativas (Vitrine)", description = "Retorna apenas categorias marcadas como ativas.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    ResponseEntity<Page<CategoriaResponseDTO>> listarAtivas(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Buscar categoria por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada", content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    })
    ResponseEntity<CategoriaResponseDTO> buscarPorId(@Parameter(description = "ID da categoria") @PathVariable String id);

    @Operation(summary = "Cadastrar nova categoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso", content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou nome/slug duplicado", content = @Content)
    })
    ResponseEntity<CategoriaResponseDTO> criar(@RequestBody @Valid Categoria obj);

    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso", content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    })
    ResponseEntity<CategoriaResponseDTO> atualizar(@Parameter(description = "ID da categoria") @PathVariable String id, @RequestBody @Valid Categoria obj);

    @Operation(summary = "Excluir categoria (Físico)", description = "Remove a categoria do banco de dados. Caso possua produtos ou subcategorias, retornará erro de integridade.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
        @ApiResponse(responseCode = "409", description = "Violação de integridade (possui produtos vinculados)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    })
    ResponseEntity<Void> excluir(@Parameter(description = "ID da categoria") @PathVariable String id);

    @Operation(summary = "Desativar categoria", description = "Realiza a exclusão lógica, removendo a categoria da vitrine.")
    @ApiResponse(responseCode = "204", description = "Categoria desativada com sucesso")
    ResponseEntity<Void> desativar(@Parameter(description = "ID da categoria") @PathVariable String id);

    @Operation(summary = "Ativar categoria", description = "Torna a categoria visível novamente.")
    @ApiResponse(responseCode = "204", description = "Categoria ativada com sucesso")
    ResponseEntity<Void> ativar(@Parameter(description = "ID da categoria") @PathVariable String id);
}
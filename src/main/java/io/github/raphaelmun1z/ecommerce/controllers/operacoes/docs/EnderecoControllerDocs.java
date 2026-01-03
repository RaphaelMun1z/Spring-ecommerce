package io.github.raphaelmun1z.ecommerce.controllers.operacoes.docs;

import io.github.raphaelmun1z.ecommerce.dtos.req.usuario.EnderecoRequestDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.usuario.EnderecoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Endereços", description = "Gestão do livro de endereços do cliente")
public interface EnderecoControllerDocs {

    @Operation(summary = "Listar endereços do cliente")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    ResponseEntity<List<EnderecoResponseDTO>> listar(@Parameter(description = "ID do cliente") @PathVariable String clienteId);

    @Operation(summary = "Adicionar novo endereço")
    @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso")
    ResponseEntity<EnderecoResponseDTO> criar(
        @Parameter(description = "ID do cliente") @PathVariable String clienteId,
        @RequestBody @Valid EnderecoRequestDTO dto
    );

    @Operation(summary = "Atualizar endereço")
    @ApiResponse(responseCode = "200", description = "Endereço atualizado")
    ResponseEntity<EnderecoResponseDTO> atualizar(
        @Parameter(description = "ID do endereço") @PathVariable String id,
        @RequestBody @Valid EnderecoRequestDTO dto
    );

    @Operation(summary = "Remover endereço")
    @ApiResponse(responseCode = "204", description = "Endereço removido")
    ResponseEntity<Void> deletar(@Parameter(description = "ID do endereço") @PathVariable String id);

    @Operation(summary = "Definir endereço como principal", description = "Marca este endereço como padrão e desmarca os outros.")
    @ApiResponse(responseCode = "204", description = "Endereço principal atualizado")
    ResponseEntity<Void> definirPrincipal(@Parameter(description = "ID do endereço") @PathVariable String id);
}
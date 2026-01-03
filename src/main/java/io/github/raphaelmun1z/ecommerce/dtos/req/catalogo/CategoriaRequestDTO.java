package io.github.raphaelmun1z.ecommerce.dtos.req.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação e atualização de categorias")
public class CategoriaRequestDTO {

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.")
    @Schema(description = "Nome da categoria", example = "Ferramentas Elétricas")
    private String nome;

    @Size(max = 500, message = "A descrição não pode exceder 500 caracteres.")
    @Schema(description = "Descrição detalhada", example = "Furadeiras, serras e equipamentos diversos.")
    private String descricao;

    @NotBlank(message = "O slug é obrigatório.")
    @Schema(description = "Slug URL amigável", example = "ferramentas-eletricas")
    private String slug;

    @Schema(description = "Define se a categoria já nasce ativa", example = "true")
    private Boolean ativa = true;

    @Schema(description = "ID da categoria pai (para subcategorias)", example = "550e8400-e29b-41d4-a716-446655440000")
    private String categoriaPaiId;

    public CategoriaRequestDTO() {
    }

    public CategoriaRequestDTO(String nome, String descricao, String slug, Boolean ativa, String categoriaPaiId) {
        this.nome = nome;
        this.descricao = descricao;
        this.slug = slug;
        this.ativa = ativa;
        this.categoriaPaiId = categoriaPaiId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public String getCategoriaPaiId() {
        return categoriaPaiId;
    }

    public void setCategoriaPaiId(String categoriaPaiId) {
        this.categoriaPaiId = categoriaPaiId;
    }
}
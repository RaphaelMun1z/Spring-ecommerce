package io.github.raphaelmun1z.ecommerce.dtos.res.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO responsável por apresentar os dados de uma categoria e sua estrutura hierárquica")
public class CategoriaResponseDTO {

    @Schema(description = "Identificador único da categoria", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Nome da categoria", example = "Ferramentas Elétricas")
    private String nome;

    @Schema(description = "Descrição detalhada", example = "Furadeiras, serras, lixadeiras e outros equipamentos.")
    private String descricao;

    @Schema(description = "Slug URL amigável", example = "ferramentas-eletricas")
    private String slug;

    @Schema(description = "Indica se a categoria está ativa", example = "true")
    private Boolean ativa;

    @Schema(description = "ID da categoria pai (caso seja uma subcategoria)", example = "550e8400-e29b-41d4-a716-446655440000")
    private String categoriaPaiId;

    @Schema(description = "Lista de subcategorias filhas desta categoria")
    private List<CategoriaResponseDTO> subcategorias = new ArrayList<>();

    public CategoriaResponseDTO() {
    }

    public CategoriaResponseDTO(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
        this.descricao = categoria.getDescricao();
        this.slug = categoria.getSlug();
        this.ativa = categoria.getAtiva();

        if (categoria.getCategoriaPai() != null) {
            this.categoriaPaiId = categoria.getCategoriaPai().getId();
        }

        if (categoria.getSubcategorias() != null && !categoria.getSubcategorias().isEmpty()) {
            this.subcategorias = categoria.getSubcategorias().stream()
                .map(CategoriaResponseDTO::new)
                .collect(Collectors.toList());
        }
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<CategoriaResponseDTO> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<CategoriaResponseDTO> subcategorias) {
        this.subcategorias = subcategorias;
    }
}
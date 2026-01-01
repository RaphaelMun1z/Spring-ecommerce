package io.github.raphaelmun1z.ecommerce.dtos.res;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;

import java.math.BigDecimal;

public class ProdutoResponseDTO {

    private String id;
    private String titulo;
    private String descricao;
    private BigDecimal preco;
    private BigDecimal precoPromocional;
    private Integer estoque;
    private Boolean ativo;
    private String categoriaNome;

    public ProdutoResponseDTO(Produto entity) {
        this.id = entity.getId();
        this.titulo = entity.getTitulo();
        this.descricao = entity.getDescricao();
        this.preco = entity.getPreco();
        this.precoPromocional = entity.getPrecoPromocional();
        this.estoque = entity.getEstoque();
        this.ativo = entity.getAtivo();
        this.categoriaNome = entity.getCategoria() != null ? entity.getCategoria().getNome() : null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getPrecoPromocional() {
        return precoPromocional;
    }

    public void setPrecoPromocional(BigDecimal precoPromocional) {
        this.precoPromocional = precoPromocional;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }
}
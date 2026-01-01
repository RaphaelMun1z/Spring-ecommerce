package io.github.raphaelmun1z.ecommerce.entities.catalogo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_produto",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"cod_controle"})},
    indexes = {@Index(name = "idx_produto_titulo", columnList = "titulo")}
)
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "O código de controle (SKU) é obrigatório.")
    @Column(name = "cod_controle", nullable = false, length = 50, updatable = false)
    private String codigoControle;

    @NotBlank(message = "O título do produto é obrigatório.")
    @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres.")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória.")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2) // Ex: 99999999.99
    private BigDecimal preco;

    @Column(name = "preco_promocional", precision = 10, scale = 2)
    private BigDecimal precoPromocional;

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "O estoque não pode ser negativo.")
    @Column(nullable = false)
    private Integer estoque;

    @Column(nullable = false)
    private Boolean ativo = true;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = true)
    private Double pesoKg;

    @Column(length = 20)
    private String dimensoes;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagemProduto> imagens = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    public Produto() {
    }

    public Produto(String codigoControle, String titulo, String descricao, BigDecimal preco, BigDecimal precoPromocional, Integer estoque, Boolean ativo, Double pesoKg, String dimensoes, Categoria categoria, List<ImagemProduto> imagens, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.codigoControle = codigoControle;
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.precoPromocional = precoPromocional;
        this.estoque = estoque;
        this.ativo = ativo;
        this.pesoKg = pesoKg;
        this.dimensoes = dimensoes;
        this.categoria = categoria;
        this.imagens = imagens;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getId() {
        return id;
    }

    public String getCodigoControle() {
        return codigoControle;
    }

    public void setCodigoControle(String codigoControle) {
        this.codigoControle = codigoControle;
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

    public Double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(Double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public String getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<ImagemProduto> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemProduto> imagens) {
        this.imagens = imagens;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}

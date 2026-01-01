package io.github.raphaelmun1z.ecommerce.entities.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidade principal do catálogo representando um produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único do produto", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @NotBlank(message = "O código de controle (SKU) é obrigatório.")
    @Column(name = "cod_controle", nullable = false, length = 50, updatable = false)
    @Schema(description = "Código de controle único (SKU)", example = "FER-001-BOSCH")
    private String codigoControle;

    @NotBlank(message = "O título do produto é obrigatório.")
    @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres.")
    @Column(nullable = false, length = 150)
    @Schema(description = "Nome/Título do produto", example = "Furadeira de Impacto 650W")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória.")
    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "Descrição detalhada do produto", example = "Ideal para trabalhos profissionais...")
    private String descricao;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2) // Ex: 99999999.99
    @Schema(description = "Preço de venda padrão", example = "299.90")
    private BigDecimal preco;

    @Column(name = "preco_promocional", precision = 10, scale = 2)
    @Schema(description = "Preço promocional temporário", example = "249.90", nullable = true)
    private BigDecimal precoPromocional;

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "O estoque não pode ser negativo.")
    @Column(nullable = false)
    @Schema(description = "Quantidade disponível em estoque", example = "50")
    private Integer estoque;

    @Column(nullable = false)
    @Schema(description = "Indica se o produto está ativo e visível na vitrine", example = "true")
    private Boolean ativo = true;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = true)
    @Schema(description = "Peso do produto em KG para cálculo de frete", example = "1.5")
    private Double pesoKg;

    @Column(length = 20)
    @Schema(description = "Dimensões físicas do produto (AxLxP)", example = "30x10x20")
    private String dimensoes;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @Schema(description = "Categoria à qual o produto pertence")
    private Categoria categoria;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de imagens do produto")
    private List<ImagemProduto> imagens = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    @Schema(description = "Data e hora de criação do registro")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    @Schema(description = "Data e hora da última atualização")
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
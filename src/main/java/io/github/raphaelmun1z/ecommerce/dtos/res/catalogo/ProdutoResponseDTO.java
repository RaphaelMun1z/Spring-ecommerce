package io.github.raphaelmun1z.ecommerce.dtos.res.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.ImagemProduto;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO de resposta contendo os dados públicos do produto")
public class ProdutoResponseDTO {

    @Schema(description = "ID do produto", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Código de controle (SKU)", example = "FER-001-BOSCH")
    private String codigoControle;

    @Schema(description = "Título do produto", example = "Furadeira de Impacto")
    private String titulo;

    @Schema(description = "Descrição", example = "Ideal para uso profissional...")
    private String descricao;

    @Schema(description = "Preço original", example = "299.90")
    private BigDecimal preco;

    @Schema(description = "Preço promocional (se houver)", example = "249.90")
    private BigDecimal precoPromocional;

    @Schema(description = "Estoque atual", example = "50")
    private Integer estoque;

    @Schema(description = "Status de ativação", example = "true")
    private Boolean ativo;

    @Schema(description = "Dimensões", example = "30x10x20")
    private String dimensoes;

    @Schema(description = "Peso (kg)", example = "1.5")
    private Double pesoKg;

    @Schema(description = "Dados da categoria vinculada")
    private CategoriaResponseDTO categoria;

    @Schema(description = "Lista de objetos de imagem (com ordem e flag principal)")
    private List<ImagemProdutoResponseDTO> imagens;

    public ProdutoResponseDTO() {
    }

    public ProdutoResponseDTO(Produto entity) {
        this.id = entity.getId();
        this.codigoControle = entity.getCodigoControle();
        this.titulo = entity.getTitulo();
        this.descricao = entity.getDescricao();
        this.preco = entity.getPreco();
        this.precoPromocional = entity.getPrecoPromocional();
        this.estoque = entity.getEstoque();
        this.ativo = entity.getAtivo();
        this.dimensoes = entity.getDimensoes();
        this.pesoKg = entity.getPesoKg();

        if (entity.getCategoria() != null) {
            this.categoria = new CategoriaResponseDTO(entity.getCategoria());
        }

        // Inicializa a lista para evitar null
        this.imagens = new ArrayList<>();

        if (entity.getImagens() != null && !entity.getImagens().isEmpty()) {
            this.imagens = entity.getImagens().stream()
                .sorted(Comparator.comparing(ImagemProduto::getOrdem, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(ImagemProdutoResponseDTO::new)
                .collect(Collectors.toList());
        }
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCodigoControle() { return codigoControle; }
    public void setCodigoControle(String codigoControle) { this.codigoControle = codigoControle; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public BigDecimal getPrecoPromocional() { return precoPromocional; }
    public void setPrecoPromocional(BigDecimal precoPromocional) { this.precoPromocional = precoPromocional; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public String getDimensoes() { return dimensoes; }
    public void setDimensoes(String dimensoes) { this.dimensoes = dimensoes; }

    public Double getPesoKg() { return pesoKg; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }

    public CategoriaResponseDTO getCategoria() { return categoria; }
    public void setCategoria(CategoriaResponseDTO categoria) { this.categoria = categoria; }

    public List<ImagemProdutoResponseDTO> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemProdutoResponseDTO> imagens) {
        this.imagens = imagens;
    }
}
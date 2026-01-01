package io.github.raphaelmun1z.ecommerce.entities.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_imagem_produto")
@Schema(description = "Entidade que representa uma imagem associada a um produto")
public class ImagemProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da imagem", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @NotBlank(message = "A URL da imagem é obrigatória.")
    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "URL onde a imagem está hospedada", example = "https://bucket.s3.amazonaws.com/produtos/furadeira.jpg")
    private String url;

    @Column(nullable = false)
    @Schema(description = "Ordem de exibição na galeria (0, 1, 2...)", example = "0")
    private Integer ordem;

    @Column(nullable = false)
    @Schema(description = "Indica se esta é a imagem principal (capa) do produto", example = "true")
    private Boolean principal = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    @JsonIgnore
    @Schema(description = "Produto ao qual a imagem pertence")
    private Produto produto;

    public ImagemProduto() {
    }

    public ImagemProduto(String url, Integer ordem, Boolean principal, Produto produto) {
        this.url = url;
        this.ordem = ordem;
        this.principal = principal;
        this.produto = produto;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
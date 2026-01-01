package io.github.raphaelmun1z.ecommerce.entities.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_imagem_produto")
public class ImagemProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "A URL da imagem é obrigatória.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private Boolean principal = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    @JsonIgnore
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
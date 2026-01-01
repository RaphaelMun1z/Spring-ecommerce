package io.github.raphaelmun1z.ecommerce.entities.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_categoria",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "nome"),
        @UniqueConstraint(columnNames = "slug")
    },
    indexes = {
        @Index(name = "idx_categoria_nome", columnList = "nome"),
        @Index(name = "idx_categoria_slug", columnList = "slug")
    }
)
@Schema(description = "Entidade que representa uma categoria de produtos, podendo ter uma estrutura hierárquica")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da categoria", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.")
    @Column(nullable = false, length = 50)
    @Schema(description = "Nome da categoria", example = "Ferramentas Elétricas")
    private String nome;

    @Size(max = 500, message = "A descrição não pode exceder 500 caracteres.")
    @Column(length = 500)
    @Schema(description = "Descrição detalhada da categoria", example = "Furadeiras, serras, lixadeiras e outros equipamentos elétricos.")
    private String descricao;

    @NotBlank(message = "O slug é obrigatório.")
    @Column(nullable = false, length = 60)
    @Schema(description = "Slug URL amigável para a categoria", example = "ferramentas-eletricas")
    private String slug;

    @Column(nullable = false)
    @Schema(description = "Indica se a categoria está ativa e visível na loja", example = "true")
    private Boolean ativa = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_pai_id")
    @Schema(description = "Categoria pai (superior), caso esta seja uma subcategoria")
    private Categoria categoriaPai;

    @OneToMany(mappedBy = "categoriaPai", cascade = CascadeType.ALL)
    @Schema(description = "Lista de subcategorias filhas")
    private List<Categoria> subcategorias = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    @Schema(description = "Data e hora de criação da categoria")
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    @Schema(description = "Data e hora da última atualização da categoria")
    private LocalDateTime dataAtualizacao;

    public Categoria() {
    }

    public Categoria(String nome, String slug) {
        this.nome = nome;
        this.slug = slug;
    }

    public String getId() {
        return id;
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

    public Categoria getCategoriaPai() {
        return categoriaPai;
    }

    public void setCategoriaPai(Categoria categoriaPai) {
        this.categoriaPai = categoriaPai;
    }

    public List<Categoria> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<Categoria> subcategorias) {
        this.subcategorias = subcategorias;
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
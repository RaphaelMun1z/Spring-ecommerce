package io.github.raphaelmun1z.ecommerce.entities.catalogo;

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
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.")
    @Column(nullable = false, length = 50)
    private String nome;

    @Size(max = 500, message = "A descrição não pode exceder 500 caracteres.")
    @Column(length = 500)
    private String descricao;

    @NotBlank(message = "O slug é obrigatório.")
    @Column(nullable = false, length = 60)
    private String slug;

    @Column(nullable = false)
    private Boolean ativa = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_pai_id")
    private Categoria categoriaPai;

    @OneToMany(mappedBy = "categoriaPai", cascade = CascadeType.ALL)
    private List<Categoria> subcategorias = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
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
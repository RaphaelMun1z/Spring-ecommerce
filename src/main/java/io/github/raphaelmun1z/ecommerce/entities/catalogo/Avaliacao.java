package io.github.raphaelmun1z.ecommerce.entities.catalogo;

import io.github.raphaelmun1z.ecommerce.entities.usuario.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_avaliacao",
    indexes = {
        @Index(name = "idx_avaliacao_produto", columnList = "produto_id"),
        @Index(name = "idx_avaliacao_usuario", columnList = "usuario_id")
    }
)
@Schema(description = "Entidade que representa uma avaliação feita por um usuário sobre um produto")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da avaliação", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @NotNull(message = "A nota é obrigatória.")
    @Min(value = 1, message = "A nota deve ser no mínimo 1.")
    @Max(value = 5, message = "A nota deve ser no máximo 5.")
    @Column(nullable = false)
    @Schema(description = "Nota dada ao produto (1 a 5 estrelas)", example = "5")
    private Integer nota;

    @Size(max = 100, message = "O título da avaliação deve ter no máximo 100 caracteres.")
    @Column(length = 100)
    @Schema(description = "Título resumido da avaliação", example = "Excelente produto!")
    private String titulo;

    @NotBlank(message = "O comentário é obrigatório.")
    @Size(max = 2000, message = "O comentário não pode exceder 2000 caracteres.")
    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "Comentário detalhado sobre a experiência com o produto", example = "A furadeira é muito potente e fácil de usar. Recomendo!")
    private String comentario;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    @Schema(description = "Data e hora em que a avaliação foi criada")
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    @Schema(description = "Indica se a avaliação está visível publicamente (moderação)", example = "true")
    private Boolean visivel = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    @Schema(description = "Produto que está sendo avaliado")
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuário que realizou a avaliação")
    private Usuario usuario;

    public Avaliacao() {
    }

    public Avaliacao(Integer nota, String titulo, String comentario, Produto produto, Usuario usuario) {
        this.nota = nota;
        this.titulo = titulo;
        this.comentario = comentario;
        this.produto = produto;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Boolean getVisivel() {
        return visivel;
    }

    public void setVisivel(Boolean visivel) {
        this.visivel = visivel;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
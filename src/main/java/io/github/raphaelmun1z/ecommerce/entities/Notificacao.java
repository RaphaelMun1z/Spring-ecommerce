package io.github.raphaelmun1z.ecommerce.entities;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEnvio;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoCanal;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notificacao", indexes = {
    @Index(name = "idx_notificacao_cliente", columnList = "cliente_id"),
    @Index(name = "idx_notificacao_data", columnList = "data_envio")
})
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @NotBlank(message = "O destinatário é obrigatório.")
    @Column(nullable = false)
    private String destinatario;

    @NotBlank(message = "O título/assunto é obrigatório.")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "A mensagem é obrigatória.")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensagem;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_canal", nullable = false, length = 20)
    private TipoCanal canal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEnvio status;

    @CreationTimestamp
    @Column(name = "data_envio", updatable = false)
    private LocalDateTime dataEnvio;

    public Notificacao() {
    }

    public Notificacao(Cliente cliente, String destinatario, String titulo, String mensagem, TipoCanal canal, StatusEnvio status) {
        this.cliente = cliente;
        this.destinatario = destinatario;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.canal = canal;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public TipoCanal getCanal() {
        return canal;
    }

    public void setCanal(TipoCanal canal) {
        this.canal = canal;
    }

    public StatusEnvio getStatus() {
        return status;
    }

    public void setStatus(StatusEnvio status) {
        this.status = status;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }
}
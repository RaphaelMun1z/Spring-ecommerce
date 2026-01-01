package io.github.raphaelmun1z.ecommerce.entities;

import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEnvio;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoCanal;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidade que registra o histórico de notificações enviadas aos clientes (e-mail, SMS, etc)")
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da notificação", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @Schema(description = "Cliente associado à notificação (opcional)")
    private Cliente cliente;

    @NotBlank(message = "O destinatário é obrigatório.")
    @Column(nullable = false)
    @Schema(description = "Endereço ou número de destino (email/telefone)", example = "cliente@email.com")
    private String destinatario;

    @NotBlank(message = "O título/assunto é obrigatório.")
    @Column(nullable = false)
    @Schema(description = "Título ou assunto da notificação", example = "Seu pedido foi enviado!")
    private String titulo;

    @NotBlank(message = "A mensagem é obrigatória.")
    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "Corpo da mensagem enviada", example = "Olá, seu pedido #1234 já está com a transportadora.")
    private String mensagem;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_canal", nullable = false, length = 20)
    @Schema(description = "Canal de envio utilizado", example = "EMAIL")
    private TipoCanal canal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "Status do envio da notificação", example = "ENVIADO")
    private StatusEnvio status;

    @CreationTimestamp
    @Column(name = "data_envio", updatable = false)
    @Schema(description = "Data e hora do envio")
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
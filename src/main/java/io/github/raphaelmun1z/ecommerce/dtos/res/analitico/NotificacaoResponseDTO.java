package io.github.raphaelmun1z.ecommerce.dtos.res.analitico;

import io.github.raphaelmun1z.ecommerce.entities.Notificacao;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEnvio;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoCanal;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO contendo os detalhes da notificação enviada")
public class NotificacaoResponseDTO {

    @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "ID do cliente associado", example = "550e8400-e29b-41d4-a716-446655440000")
    private String clienteId;

    @Schema(description = "Destinatário (email/telefone)", example = "cliente@email.com")
    private String destinatario;

    @Schema(description = "Título da mensagem", example = "Seu pedido foi enviado!")
    private String titulo;

    @Schema(description = "Conteúdo da mensagem", example = "Olá, seu pedido #1234...")
    private String mensagem;

    @Schema(description = "Canal utilizado", example = "EMAIL")
    private TipoCanal canal;

    @Schema(description = "Status do envio", example = "ENVIADO")
    private StatusEnvio status;

    @Schema(description = "Data do envio")
    private LocalDateTime dataEnvio;

    public NotificacaoResponseDTO() {
    }

    public NotificacaoResponseDTO(Notificacao entity) {
        this.id = entity.getId();
        this.destinatario = entity.getDestinatario();
        this.titulo = entity.getTitulo();
        this.mensagem = entity.getMensagem();
        this.canal = entity.getCanal();
        this.status = entity.getStatus();
        this.dataEnvio = entity.getDataEnvio();

        if (entity.getCliente() != null) {
            this.clienteId = entity.getCliente().getId();
        }
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
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

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}
package io.github.raphaelmun1z.ecommerce.services.autorizacao;

public interface EmailService {
    void enviarEmailRedefinicaoSenha(String para, String token);
}
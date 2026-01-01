package io.github.raphaelmun1z.ecommerce.services.autorizacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void enviarEmailRedefinicaoSenha(String para, String token) {
        String link = "http://localhost:3000/redefinir-senha?token=" + token;
        logger.info("--- SIMULANDO ENVIO DE EMAIL DE REDEFINIÇÃO DE SENHA ---");
        logger.info("Para: {}", para);
        logger.info("Link: {}", link);
        logger.info("------------------------------------------------------");
    }
}
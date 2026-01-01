package io.github.raphaelmun1z.ecommerce.config;

import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PapelRepository;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PermissaoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@Slf4j
@Profile("test")
public class DataInitializer implements CommandLineRunner {

    public DataInitializer() {
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Init DataInitializer...");
    }
}
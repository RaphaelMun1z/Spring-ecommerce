package io.github.raphaelmun1z.ecommerce.config;

import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Permissao;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PapelRepository;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PermissaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@Order(1)
public class PermissionInitializer implements ApplicationRunner {

    private final PermissaoRepository permissaoRepository;
    private final PapelRepository papelRepository;

    public PermissionInitializer(PermissaoRepository permissaoRepository, PapelRepository papelRepository) {
        this.permissaoRepository = permissaoRepository;
        this.papelRepository = papelRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("Inicializando Permissões e Papéis no Banco de Dados...");

        // 1. Criar as Permissões
        Permissao permSalvarProduto = createPermissionIfNotFound("PRODUTO_INSERIR");
        Permissao permBaixarEstoque = createPermissionIfNotFound("ESTOQUE_DECREMENTAR");

        // 2. Definir conjuntos de permissões
        Set<Permissao> adminPermissions = new HashSet<>(Arrays.asList(permSalvarProduto, permBaixarEstoque));
        Set<Permissao> clientePermissions = new HashSet<>();

        // 3. Criar as Roles (Papéis)
        createRoleIfNotFound("ROLE_ADMIN", adminPermissions);
        createRoleIfNotFound("ROLE_CLIENTE", clientePermissions);

        log.info("Inicialização de segurança concluída.");
    }

    @Transactional
    Permissao createPermissionIfNotFound(String nome) {
        Permissao permissao = permissaoRepository.findByNome(nome).orElse(null);

        if (permissao == null) {
            permissao = new Permissao();
            permissao.setNome(nome);
            permissao = permissaoRepository.save(permissao);
            log.info("Permissão criada: {}", nome);
        }
        return permissao;
    }

    @Transactional
    Papel createRoleIfNotFound(String nome, Set<Permissao> permissoes) {
        Papel papel = papelRepository.findByNome(nome).orElse(null);

        if (papel == null) {
            papel = new Papel();
            papel.setNome(nome);
            papel.setPermissoes(permissoes);
            papel = papelRepository.save(papel);
            log.info("Papel criado: {}", nome);
        }
        return papel;
    }
}
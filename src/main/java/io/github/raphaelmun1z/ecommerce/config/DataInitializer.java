package io.github.raphaelmun1z.ecommerce.config;

import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Permissao;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Categoria;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PapelRepository;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PermissaoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.CategoriaRepository;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.ClienteRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@Profile({"test", "dev"})
public class DataInitializer implements CommandLineRunner {

    private final PapelRepository papelRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PapelRepository papelRepository,
                           UsuarioRepository usuarioRepository,
                           ClienteRepository clienteRepository,
                           CategoriaRepository categoriaRepository,
                           ProdutoRepository produtoRepository,
                           PasswordEncoder passwordEncoder) {
        this.papelRepository = papelRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("### Iniciando Carga de Dados (Mock) para Testes ###");

        // Verifica se já existem usuários para evitar violação de Unique Constraint (email)
        if (clienteRepository.count() > 0) {
            log.info("Dados de usuários já existem no banco. Pulando inicialização.");
            return;
        }

        // 1. Recuperar Roles (já criadas pelo PermissionInitializer)
        // O PermissionInitializer roda via ApplicationRunner, que geralmente precede ou corre junto.
        // Verificamos se existem para evitar NullPointerException.
        Papel roleAdmin = papelRepository.findByNome("ROLE_ADMIN")
            .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN não encontrada. Verifique se o PermissionInitializer rodou."));

        Papel roleCliente = papelRepository.findByNome("ROLE_CLIENTE")
            .orElseThrow(() -> new IllegalStateException("ROLE_CLIENTE não encontrada. Verifique se o PermissionInitializer rodou."));

        // 2. Criar Usuários (Senha padrão: 123456)
        String senhaCodificada = passwordEncoder.encode("123456");

        // Cria um Cliente Padrão (Carlos)
        // Usa a classe Cliente que estende Usuario (herdando nome, email, senha, papel)
        Cliente carlos = new Cliente();
        carlos.setNome("Carlos Souza");
        carlos.setEmail("carlos@email.com");
        carlos.setSenha(senhaCodificada);
        carlos.setPapel(roleCliente);
        // Campos específicos de Cliente
        carlos.setCpf("12345678900");
        carlos.setTelefone("11999999999");

        clienteRepository.save(carlos);

        // Cria um Admin (Usando Cliente com role de Admin para simplificar, ou use entidade Administrador se houver)
        Cliente admin = new Cliente();
        admin.setNome("Administrador do Sistema");
        admin.setEmail("admin@email.com");
        admin.setSenha(senhaCodificada);
        admin.setPapel(roleAdmin);
        admin.setCpf("00000000000");
        admin.setTelefone("000000000");

        clienteRepository.save(admin);

        log.info("Usuários Mock Criados:");
        log.info(" > Cliente: carlos@email.com / 123456");
        log.info(" > Admin:   admin@email.com / 123456");

        // 3. Criar Categorias (se não existirem)
        if (categoriaRepository.count() == 0) {
            Categoria catEletronicos = new Categoria("Eletrônicos", "eletronicos");
            catEletronicos.setDescricao("Gadgets e dispositivos gerais");

            Categoria catComputadores = new Categoria("Computadores", "computadores");
            catComputadores.setDescricao("Notebooks, PCs e acessórios");
            catComputadores.setCategoriaPai(catEletronicos); // Hierarquia

            Categoria catLivros = new Categoria("Livros", "livros");

            categoriaRepository.saveAll(Arrays.asList(catEletronicos, catComputadores, catLivros));

            // 4. Criar Produtos Mock
            Produto p1 = new Produto();
            p1.setCodigoControle("SKU-NOTE-DELL");
            p1.setTitulo("Notebook Dell XPS 13");
            p1.setDescricao("Notebook ultra-fino, i7, 16GB RAM, SSD 512GB.");
            p1.setPreco(new BigDecimal("8500.00"));
            p1.setEstoque(10);
            p1.setAtivo(true);
            p1.setPesoKg(1.2);
            p1.setCategoria(catComputadores);

            Produto p2 = new Produto();
            p2.setCodigoControle("SKU-SMART-S23");
            p2.setTitulo("Smartphone Samsung Galaxy S23");
            p2.setDescricao("128GB, Tela 6.1, Câmera Tripla.");
            p2.setPreco(new BigDecimal("4500.00"));
            p2.setPrecoPromocional(new BigDecimal("3999.90"));
            p2.setEstoque(50);
            p2.setAtivo(true);
            p2.setPesoKg(0.3);
            p2.setCategoria(catEletronicos);

            Produto p3 = new Produto();
            p3.setCodigoControle("SKU-LIVRO-JAVA");
            p3.setTitulo("Java Efetivo");
            p3.setDescricao("As melhores práticas para a plataforma Java.");
            p3.setPreco(new BigDecimal("150.00"));
            p3.setEstoque(100);
            p3.setAtivo(true);
            p3.setPesoKg(0.5);
            p3.setCategoria(catLivros);

            produtoRepository.saveAll(Arrays.asList(p1, p2, p3));

            log.info("Produtos Mock Criados: Notebook, Smartphone, Livro");
        }

        log.info("### Carga de Dados Finalizada ###");
    }
}
package io.github.raphaelmun1z.ecommerce.config;

import io.github.raphaelmun1z.ecommerce.entities.Notificacao;
import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Permissao;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Categoria;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusEnvio;
import io.github.raphaelmun1z.ecommerce.entities.enums.TipoCanal;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.repositories.analitico.NotificacaoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PapelRepository;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PermissaoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.CategoriaRepository;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.ClienteRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@Profile({"test", "dev"})
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private final PapelRepository papelRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PapelRepository papelRepository,
                           UsuarioRepository usuarioRepository,
                           ClienteRepository clienteRepository,
                           CategoriaRepository categoriaRepository,
                           ProdutoRepository produtoRepository,
                           NotificacaoRepository notificacaoRepository,
                           PasswordEncoder passwordEncoder) {
        this.papelRepository = papelRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("### Iniciando Carga de Dados MASSIVA (Mock) ###");

        if (clienteRepository.count() > 0) {
            log.info("Dados já existem. Pulando inicialização.");
            return;
        }

        // --- 1. Roles ---
        Papel roleAdmin = papelRepository.findByNome("ROLE_ADMIN").orElseThrow();
        Papel roleCliente = papelRepository.findByNome("ROLE_CLIENTE").orElseThrow();

        String senhaPadrao = passwordEncoder.encode("123456");

        // --- 2. Usuários Diversificados ---
        List<Cliente> usuarios = new ArrayList<>();

        // Admin
        Cliente admin = new Cliente("Administrador Root", "admin@email.com", senhaPadrao, roleAdmin, "00000000000", "11900000000");
        usuarios.add(admin);

        // Clientes
        Cliente carlos = new Cliente("Carlos Souza", "carlos@email.com", senhaPadrao, roleCliente, "12345678900", "11999990001");
        Cliente ana = new Cliente("Ana Pereira", "ana@email.com", senhaPadrao, roleCliente, "12345678901", "11999990002");
        Cliente bruno = new Cliente("Bruno Lima", "bruno@email.com", senhaPadrao, roleCliente, "12345678902", "11999990003");
        Cliente fernanda = new Cliente("Fernanda Costa", "fernanda@email.com", senhaPadrao, roleCliente, "12345678903", "11999990004");

        clienteRepository.saveAll(Arrays.asList(admin, carlos, ana, bruno, fernanda));
        log.info("5 Usuários criados.");

        // --- 3. Notificações ---
        List<Notificacao> notificacoes = Arrays.asList(
            new Notificacao(carlos, carlos.getEmail(), "Bem-vindo!", "Sua conta foi criada.", TipoCanal.EMAIL, StatusEnvio.ENVIADO),
            new Notificacao(carlos, carlos.getTelefone(), "Promoção", "50% OFF hoje.", TipoCanal.SMS, StatusEnvio.ENVIADO),
            new Notificacao(ana, ana.getEmail(), "Pedido Enviado", "Seu pedido #123 saiu.", TipoCanal.EMAIL, StatusEnvio.ENVIADO),
            new Notificacao(bruno, bruno.getEmail(), "Carrinho Esquecido", "Volte para finalizar.", TipoCanal.EMAIL, StatusEnvio.PENDENTE)
        );
        notificacaoRepository.saveAll(notificacoes);

        // --- 4. Categorias (Estrutura Hierárquica) ---
        // Pai: Tecnologia
        Categoria catTecnologia = new Categoria("Tecnologia", "tecnologia");
        catTecnologia.setDescricao("Tudo sobre tech");

        // Filhos de Tecnologia
        Categoria catSmartphones = new Categoria("Smartphones", "smartphones");
        catSmartphones.setCategoriaPai(catTecnologia);

        Categoria catNotebooks = new Categoria("Notebooks", "notebooks");
        catNotebooks.setCategoriaPai(catTecnologia);

        Categoria catAcessorios = new Categoria("Acessórios Tech", "acessorios-tech");
        catAcessorios.setCategoriaPai(catTecnologia);

        // Pai: Casa e Conforto
        Categoria catCasa = new Categoria("Casa e Conforto", "casa-conforto");

        // Filhos de Casa
        Categoria catMoveis = new Categoria("Móveis", "moveis");
        catMoveis.setCategoriaPai(catCasa);

        Categoria catDecoracao = new Categoria("Decoração", "decoracao");
        catDecoracao.setCategoriaPai(catCasa);

        // Pai: Livraria
        Categoria catLivros = new Categoria("Livraria", "livraria");

        // Filhos de Livraria
        Categoria catTecnicos = new Categoria("Livros Técnicos", "livros-tecnicos");
        catTecnicos.setCategoriaPai(catLivros);

        Categoria catFiccao = new Categoria("Ficção e Fantasia", "ficcao");
        catFiccao.setCategoriaPai(catLivros);

        categoriaRepository.saveAll(Arrays.asList(
            catTecnologia, catSmartphones, catNotebooks, catAcessorios,
            catCasa, catMoveis, catDecoracao,
            catLivros, catTecnicos, catFiccao
        ));
        log.info("10 Categorias criadas.");

        // --- 5. Produtos Massivos ---
        List<Produto> produtos = new ArrayList<>();

        // Smartphones
        produtos.add(new Produto("SM-S23-ULTRA", "Samsung Galaxy S23 Ultra", "256GB, Câmera 200MP", new BigDecimal("6999.00"), new BigDecimal("6499.00"), 30, true, 0.4, "16x8x1", catSmartphones, null, null, null));
        produtos.add(new Produto("IPHONE-14-PRO", "iPhone 14 Pro", "128GB, Deep Purple", new BigDecimal("7599.00"), null, 15, true, 0.35, "15x7x1", catSmartphones, null, null, null));
        produtos.add(new Produto("XIAOMI-13", "Xiaomi 13 Lite", "Android rápido e barato", new BigDecimal("2500.00"), new BigDecimal("2199.00"), 100, true, 0.3, "15x7x1", catSmartphones, null, null, null));
        produtos.add(new Produto("MOTO-EDGE-30", "Motorola Edge 30", "5G Integrado", new BigDecimal("2800.00"), null, 40, true, 0.3, "16x7x1", catSmartphones, null, null, null));

        // Notebooks
        produtos.add(new Produto("DELL-XPS-13", "Dell XPS 13 Plus", "Core i7, 16GB RAM", new BigDecimal("11500.00"), new BigDecimal("10999.00"), 5, true, 1.1, "30x20x1", catNotebooks, null, null, null));
        produtos.add(new Produto("MACBOOK-AIR-M2", "MacBook Air M2", "Chip Apple M2, 8GB", new BigDecimal("9500.00"), null, 10, true, 1.2, "30x21x1", catNotebooks, null, null, null));
        produtos.add(new Produto("ACER-NITRO-5", "Acer Nitro 5", "Gamer, RTX 3050", new BigDecimal("4500.00"), new BigDecimal("4199.00"), 20, true, 2.3, "36x25x2", catNotebooks, null, null, null));
        produtos.add(new Produto("LENOVO-THINKPAD", "Lenovo ThinkPad E14", "Corporativo Robusto", new BigDecimal("5200.00"), null, 15, true, 1.5, "32x22x2", catNotebooks, null, null, null));

        // Acessórios
        produtos.add(new Produto("MOUSE-LOGI-MX", "Mouse Logitech MX Master 3", "Ergonômico", new BigDecimal("650.00"), null, 50, true, 0.2, "10x5x5", catAcessorios, null, null, null));
        produtos.add(new Produto("TECLADO-MECH", "Teclado Mecânico Keychron", "Switch Brown", new BigDecimal("800.00"), new BigDecimal("750.00"), 25, true, 0.8, "30x12x3", catAcessorios, null, null, null));
        produtos.add(new Produto("FONE-SONY", "Headphone Sony WH-1000XM5", "Cancelamento de Ruído", new BigDecimal("2200.00"), null, 30, true, 0.5, "20x18x8", catAcessorios, null, null, null));

        // Móveis
        produtos.add(new Produto("SOFA-3-LUG", "Sofá Retrátil 3 Lugares", "Tecido Suede Azul", new BigDecimal("2500.00"), new BigDecimal("1999.00"), 10, true, 60.0, "200x100x100", catMoveis, null, null, null));
        produtos.add(new Produto("MESA-JANTAR", "Mesa de Jantar 6 Cadeiras", "Madeira Maciça", new BigDecimal("3500.00"), null, 5, true, 80.0, "180x90x75", catMoveis, null, null, null));
        produtos.add(new Produto("CADEIRA-OFFICE", "Cadeira Ergonomica Office", "Tela Mesh", new BigDecimal("1200.00"), new BigDecimal("899.00"), 40, true, 15.0, "60x60x110", catMoveis, null, null, null));

        // Decoração
        produtos.add(new Produto("LUMINARIA-CHAO", "Luminária de Chão", "Estilo Industrial", new BigDecimal("350.00"), null, 20, true, 2.0, "30x30x150", catDecoracao, null, null, null));
        produtos.add(new Produto("TAPETE-PERSA", "Tapete Estilo Persa", "2x3 metros", new BigDecimal("800.00"), new BigDecimal("599.00"), 15, true, 5.0, "200x300x1", catDecoracao, null, null, null));
        produtos.add(new Produto("QUADRO-ABSTRATO", "Quadro Abstrato Colorido", "Canvas 50x70", new BigDecimal("150.00"), null, 30, true, 1.0, "50x70x3", catDecoracao, null, null, null));

        // Livros Técnicos
        produtos.add(new Produto("LIVRO-CLEAN-CODE", "Clean Code", "Robert C. Martin", new BigDecimal("90.00"), null, 60, true, 0.4, "23x16x2", catTecnicos, null, null, null));
        produtos.add(new Produto("LIVRO-ARQ-LIMPA", "Arquitetura Limpa", "Guia do Artesão", new BigDecimal("85.00"), new BigDecimal("70.00"), 55, true, 0.4, "23x16x2", catTecnicos, null, null, null));
        produtos.add(new Produto("LIVRO-DOMAIN-DRIVEN", "Domain-Driven Design", "Eric Evans", new BigDecimal("120.00"), null, 40, true, 0.6, "24x17x3", catTecnicos, null, null, null));
        produtos.add(new Produto("LIVRO-JAVA-EFF", "Effective Java", "Joshua Bloch", new BigDecimal("150.00"), null, 35, true, 0.5, "23x16x2", catTecnicos, null, null, null));

        // Livros Ficção
        produtos.add(new Produto("LIVRO-SENHOR-ANEIS", "O Senhor dos Anéis", "Trilogia Completa", new BigDecimal("180.00"), new BigDecimal("129.90"), 100, true, 1.5, "25x15x5", catFiccao, null, null, null));
        produtos.add(new Produto("LIVRO-HARRY-POTTER", "Harry Potter e a Pedra Filosofal", "Edição Ilustrada", new BigDecimal("110.00"), null, 80, true, 0.8, "28x22x2", catFiccao, null, null, null));
        produtos.add(new Produto("LIVRO-DUNA", "Duna", "Frank Herbert", new BigDecimal("70.00"), new BigDecimal("49.90"), 90, true, 0.6, "23x16x3", catFiccao, null, null, null));

        produtoRepository.saveAll(produtos);
        log.info("{} Produtos diversificados criados.", produtos.size());

        log.info("### Carga MASSIVA Finalizada com Sucesso ###");
    }
}
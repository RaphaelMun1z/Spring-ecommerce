package io.github.raphaelmun1z.ecommerce.config;

import io.github.raphaelmun1z.ecommerce.entities.autorizacao.Papel;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Categoria;
import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.usuario.Cliente;
import io.github.raphaelmun1z.ecommerce.repositories.autorizacao.PapelRepository;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.CategoriaRepository;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.usuario.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Profile("prod")
@Order(1)
public class DataInitializerProd implements CommandLineRunner {

    private final PapelRepository papelRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializerProd(
            PapelRepository papelRepository,
            ClienteRepository clienteRepository,
            CategoriaRepository categoriaRepository,
            ProdutoRepository produtoRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.papelRepository = papelRepository;
        this.clienteRepository = clienteRepository;
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (clienteRepository.count() > 0 || produtoRepository.count() > 0) return;

        Papel roleAdmin = papelRepository.findByNome("ROLE_ADMIN").orElseThrow();

        Cliente admin = new Cliente(
                "Administrador",
                "admin@ecommerce.com",
                passwordEncoder.encode("Admin@123"),
                roleAdmin,
                "00000000000",
                "11900000000"
        );

        clienteRepository.save(admin);

        Categoria tecnologia = new Categoria("Tecnologia", "tecnologia");
        Categoria smartphones = new Categoria("Smartphones", "smartphones");
        smartphones.setCategoriaPai(tecnologia);
        Categoria notebooks = new Categoria("Notebooks", "notebooks");
        notebooks.setCategoriaPai(tecnologia);
        Categoria acessorios = new Categoria("Acessórios", "acessorios");
        acessorios.setCategoriaPai(tecnologia);

        Categoria casa = new Categoria("Casa", "casa");
        Categoria moveis = new Categoria("Móveis", "moveis");
        moveis.setCategoriaPai(casa);
        Categoria decoracao = new Categoria("Decoração", "decoracao");
        decoracao.setCategoriaPai(casa);

        categoriaRepository.saveAll(List.of(
                tecnologia, smartphones, notebooks, acessorios,
                casa, moveis, decoracao
        ));

        List<Produto> produtos = new ArrayList<>();

        produtos.add(new Produto("P01", "Galaxy S24", "256GB", new BigDecimal("7499"), new BigDecimal("6999"), 50, true, 0.4, "16x8x1", smartphones, null, null, null));
        produtos.add(new Produto("P02", "iPhone 15 Pro", "128GB", new BigDecimal("8999"), null, 40, true, 0.35, "15x7x1", smartphones, null, null, null));
        produtos.add(new Produto("P03", "Xiaomi 14", "Android Premium", new BigDecimal("4200"), new BigDecimal("3899"), 80, true, 0.3, "15x7x1", smartphones, null, null, null));
        produtos.add(new Produto("P04", "Motorola Edge 40", "5G", new BigDecimal("3200"), null, 70, true, 0.32, "16x7x1", smartphones, null, null, null));
        produtos.add(new Produto("P05", "Samsung A55", "Intermediário", new BigDecimal("2100"), new BigDecimal("1899"), 120, true, 0.33, "16x8x1", smartphones, null, null, null));

        produtos.add(new Produto("P06", "MacBook Pro M3", "16GB RAM", new BigDecimal("14500"), null, 15, true, 1.4, "31x22x1", notebooks, null, null, null));
        produtos.add(new Produto("P07", "Dell Inspiron 15", "Core i7", new BigDecimal("6200"), new BigDecimal("5899"), 30, true, 1.9, "35x23x2", notebooks, null, null, null));
        produtos.add(new Produto("P08", "Lenovo Legion 5", "RTX 4060", new BigDecimal("8200"), null, 20, true, 2.5, "36x26x2", notebooks, null, null, null));
        produtos.add(new Produto("P09", "Acer Aspire 5", "Core i5", new BigDecimal("3900"), new BigDecimal("3599"), 45, true, 1.8, "34x23x2", notebooks, null, null, null));
        produtos.add(new Produto("P10", "HP Pavilion", "Ryzen 7", new BigDecimal("5200"), null, 35, true, 1.7, "33x22x2", notebooks, null, null, null));

        produtos.add(new Produto("P11", "Mouse Gamer", "16000 DPI", new BigDecimal("299"), null, 200, true, 0.2, "12x6x4", acessorios, null, null, null));
        produtos.add(new Produto("P12", "Teclado Mecânico", "RGB", new BigDecimal("699"), new BigDecimal("649"), 150, true, 1.0, "45x15x4", acessorios, null, null, null));
        produtos.add(new Produto("P13", "Headset Gamer", "7.1", new BigDecimal("499"), null, 130, true, 0.6, "22x20x10", acessorios, null, null, null));
        produtos.add(new Produto("P14", "Webcam Full HD", "1080p", new BigDecimal("399"), null, 90, true, 0.3, "8x6x6", acessorios, null, null, null));
        produtos.add(new Produto("P15", "Monitor 27", "QHD", new BigDecimal("1800"), new BigDecimal("1599"), 60, true, 4.5, "62x37x5", acessorios, null, null, null));

        produtos.add(new Produto("P16", "Sofá 3 Lugares", "Retrátil", new BigDecimal("3200"), new BigDecimal("2799"), 10, true, 85.0, "220x95x100", moveis, null, null, null));
        produtos.add(new Produto("P17", "Mesa Escritório", "Madeira", new BigDecimal("1100"), null, 25, true, 40.0, "140x60x75", moveis, null, null, null));
        produtos.add(new Produto("P18", "Cadeira Gamer", "Ergonômica", new BigDecimal("1600"), new BigDecimal("1299"), 30, true, 25.0, "70x70x130", moveis, null, null, null));
        produtos.add(new Produto("P19", "Guarda-Roupa", "6 Portas", new BigDecimal("2800"), null, 8, true, 120.0, "240x200x60", moveis, null, null, null));
        produtos.add(new Produto("P20", "Cama Queen", "Box", new BigDecimal("2200"), new BigDecimal("1999"), 12, true, 90.0, "200x160x50", moveis, null, null, null));

        produtos.add(new Produto("P21", "Luminária LED", "Moderna", new BigDecimal("299"), null, 100, true, 2.0, "40x40x20", decoracao, null, null, null));
        produtos.add(new Produto("P22", "Tapete Sala", "2x3m", new BigDecimal("800"), new BigDecimal("599"), 40, true, 5.0, "200x300x1", decoracao, null, null, null));
        produtos.add(new Produto("P23", "Quadro Decorativo", "Minimalista", new BigDecimal("220"), null, 80, true, 1.2, "70x50x3", decoracao, null, null, null));
        produtos.add(new Produto("P24", "Espelho Decorativo", "Redondo", new BigDecimal("450"), null, 50, true, 6.0, "90x90x3", decoracao, null, null, null));
        produtos.add(new Produto("P25", "Vaso Cerâmica", "Premium", new BigDecimal("180"), null, 70, true, 3.0, "30x30x40", decoracao, null, null, null));

        for (int i = 26; i <= 50; i++) {
            produtos.add(new Produto(
                    "P" + i,
                    "Produto Genérico " + i,
                    "Descrição produto " + i,
                    new BigDecimal("99.90"),
                    null,
                    100,
                    true,
                    1.0,
                    "10x10x10",
                    tecnologia,
                    null,
                    null,
                    null
            ));
        }

        produtoRepository.saveAll(produtos);
    }
}

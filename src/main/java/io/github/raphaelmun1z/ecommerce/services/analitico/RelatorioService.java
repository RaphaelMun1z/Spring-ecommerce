package io.github.raphaelmun1z.ecommerce.services.analitico;

import io.github.raphaelmun1z.ecommerce.entities.catalogo.Produto;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.repositories.catalogo.ProdutoRepository;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PedidoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RelatorioService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public RelatorioService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public byte[] gerarRelatorioVendas(LocalDateTime inicio, LocalDateTime fim) {
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(inicio, fim, PageRequest.of(0, 10000)).getContent();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out)) {

            // Cabeçalho CSV
            writer.println("ID Pedido;Data;Cliente;Status;Total (R$)");

            // Linhas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Pedido p : pedidos) {
                writer.printf("%s;%s;%s;%s;%s%n",
                    p.getId(),
                    p.getDataPedido().format(formatter),
                    p.getCliente().getNome(),
                    p.getStatus(),
                    p.getValorTotal().toString().replace(".", ",")
                );
            }

            writer.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar relatório de vendas: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public byte[] gerarRelatorioEstoque() {
        // Busca todos os produtos ordenados por estoque (menor para maior) para identificar baixas
        List<Produto> produtos = produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "estoque"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out)) {

            writer.println("SKU;Produto;Estoque Atual;Preço (R$);Status");

            for (Produto p : produtos) {
                writer.printf("%s;%s;%d;%s;%s%n",
                    p.getCodigoControle(), // Supondo que exista getCodigoControle()
                    p.getTitulo(),
                    p.getEstoque(),
                    p.getPreco().toString().replace(".", ","),
                    p.getAtivo() ? "Ativo" : "Inativo"
                );
            }

            writer.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar relatório de estoque: " + e.getMessage());
        }
    }
}

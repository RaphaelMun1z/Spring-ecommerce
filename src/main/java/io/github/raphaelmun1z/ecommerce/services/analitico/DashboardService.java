package io.github.raphaelmun1z.ecommerce.services.analitico;

import io.github.raphaelmun1z.ecommerce.dtos.res.analitico.ChartDataResponseDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.analitico.DashboardResponseDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.PedidoResponseDTO;
import io.github.raphaelmun1z.ecommerce.dtos.res.analitico.StatCardResponseDTO;
import io.github.raphaelmun1z.ecommerce.entities.enums.StatusPedido;
import io.github.raphaelmun1z.ecommerce.entities.pedidos.Pedido;
import io.github.raphaelmun1z.ecommerce.repositories.operacoes.PedidoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final PedidoRepository pedidoRepository;

    public DashboardService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponseDTO getVisaoGeral() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioMesAtual = agora.withDayOfMonth(1).with(LocalTime.MIN);

        LocalDateTime inicioMesAnterior = inicioMesAtual.minusMonths(1);
        LocalDateTime fimMesAnterior = inicioMesAtual.minusSeconds(1); // Último segundo do mês passado

        // --- 1. Busca Dados (Raw) ---
        // Assumindo que PedidoRepository possui o método findByDataPedidoBetween
        List<Pedido> pedidosMesAtual = pedidoRepository.findByDataPedidoBetween(inicioMesAtual, agora, PageRequest.of(0, 10000)).getContent();
        List<Pedido> pedidosMesAnterior = pedidoRepository.findByDataPedidoBetween(inicioMesAnterior, fimMesAnterior, PageRequest.of(0, 10000)).getContent();

        // --- 2. Calcula KPIs ---

        // KPI 1: Vendas Totais (Soma de pedidos válidos)
        BigDecimal totalAtual = calcularTotalVendas(pedidosMesAtual);
        BigDecimal totalAnterior = calcularTotalVendas(pedidosMesAnterior);
        StatCardResponseDTO cardVendas = criarCardFinanceiro("Vendas Totais", totalAtual, totalAnterior, "ph-currency-dollar", "bg-green-50 text-green-600");

        // KPI 2: Novos Pedidos (Contagem)
        long qtdPedidosAtual = pedidosMesAtual.size();
        long qtdPedidosAnterior = pedidosMesAnterior.size();
        StatCardResponseDTO cardPedidos = criarCardNumerico("Novos Pedidos", qtdPedidosAtual, qtdPedidosAnterior, "ph-shopping-cart", "bg-brand-50 text-brand-600");

        // KPI 3: Ticket Médio (Total / Quantidade)
        BigDecimal ticketAtual = qtdPedidosAtual > 0 ? totalAtual.divide(BigDecimal.valueOf(qtdPedidosAtual), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal ticketAnterior = qtdPedidosAnterior > 0 ? totalAnterior.divide(BigDecimal.valueOf(qtdPedidosAnterior), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        StatCardResponseDTO cardTicket = criarCardFinanceiro("Ticket Médio", ticketAtual, ticketAnterior, "ph-chart-line-up", "bg-blue-50 text-blue-600");

        // KPI 4: Cancelamentos
        long canceladosAtual = pedidosMesAtual.stream().filter(p -> p.getStatus() == StatusPedido.CANCELADO).count();
        long canceladosAnterior = pedidosMesAnterior.stream().filter(p -> p.getStatus() == StatusPedido.CANCELADO).count();
        StatCardResponseDTO cardCancelados = criarCardNumericoInvertido("Cancelamentos", canceladosAtual, canceladosAnterior, "ph-x-circle", "bg-red-50 text-red-600");

        List<StatCardResponseDTO> stats = List.of(cardVendas, cardPedidos, cardTicket, cardCancelados);

        // --- 3. Gera Gráfico (Dados Simulados para UI) ---
        List<ChartDataResponseDTO> chartData = gerarDadosGrafico();

        // --- 4. Pedidos Recentes (Mapeados para DTO) ---
        List<Pedido> recentesEntities = pedidoRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "dataPedido"))).getContent();

        List<PedidoResponseDTO> recentesDTO = recentesEntities.stream()
            .map(PedidoResponseDTO::new)
            .collect(Collectors.toList());

        return new DashboardResponseDTO(stats, chartData, recentesDTO);
    }

    // --- Métodos Auxiliares de Cálculo ---

    private BigDecimal calcularTotalVendas(List<Pedido> pedidos) {
        return pedidos.stream()
            .filter(p -> p.getStatus() != StatusPedido.CANCELADO && p.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO)
            .map(Pedido::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private StatCardResponseDTO criarCardFinanceiro(String titulo, BigDecimal atual, BigDecimal anterior, String icon, String colorClass) {
        String valorFormatado = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(atual);

        double variacao = calcularVariacao(atual.doubleValue(), anterior.doubleValue());
        boolean isPositive = variacao >= 0;
        String changeText = String.format("%+.1f%%", variacao);

        return new StatCardResponseDTO(
            titulo,
            valorFormatado,
            changeText,
            isPositive,
            "vs. mês anterior",
            icon,
            colorClass
        );
    }

    private StatCardResponseDTO criarCardNumerico(String titulo, long atual, long anterior, String icon, String colorClass) {
        double variacao = calcularVariacao((double) atual, (double) anterior);
        boolean isPositive = variacao >= 0;

        return new StatCardResponseDTO(
            titulo,
            String.valueOf(atual),
            String.format("%+.1f%%", variacao),
            isPositive,
            "vs. mês anterior",
            icon,
            colorClass
        );
    }

    private StatCardResponseDTO criarCardNumericoInvertido(String titulo, long atual, long anterior, String icon, String colorClass) {
        StatCardResponseDTO dto = criarCardNumerico(titulo, atual, anterior, icon, colorClass);
        // Para cancelamentos, aumento (positivo) é ruim
        double variacao = calcularVariacao((double) atual, (double) anterior);
        dto.setPositive(variacao <= 0);
        return dto;
    }

    private double calcularVariacao(double atual, double anterior) {
        if (anterior == 0) return atual > 0 ? 100.0 : 0.0;
        return ((atual - anterior) / anterior) * 100;
    }

    private List<ChartDataResponseDTO> gerarDadosGrafico() {
        List<ChartDataResponseDTO> data = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        Locale br = new Locale("pt", "BR");

        for (int i = 6; i >= 0; i--) {
            LocalDate dia = hoje.minusDays(i);
            String label = dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, br);
            label = label.substring(0, 1).toUpperCase() + label.substring(1).replace(".", "");

            // Simulação: altura aleatória
            int height = 20 + (int) (Math.random() * 70);
            data.add(new ChartDataResponseDTO(label, height + "%"));
        }
        return data;
    }
}
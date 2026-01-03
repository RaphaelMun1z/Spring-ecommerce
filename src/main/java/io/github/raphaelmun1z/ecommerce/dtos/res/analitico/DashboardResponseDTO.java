package io.github.raphaelmun1z.ecommerce.dtos.res.analitico;

import io.github.raphaelmun1z.ecommerce.dtos.res.operacoes.PedidoResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO agregador para a visão geral do Dashboard")
public class DashboardResponseDTO {

    @Schema(description = "Lista de cards estatísticos (KPIs)")
    private List<StatCardResponseDTO> stats;

    @Schema(description = "Dados para o gráfico de vendas semanais")
    private List<ChartDataResponseDTO> chartData;

    @Schema(description = "Lista dos pedidos mais recentes")
    private List<PedidoResponseDTO> recentOrders;

    public DashboardResponseDTO() {
    }

    public DashboardResponseDTO(List<StatCardResponseDTO> stats, List<ChartDataResponseDTO> chartData, List<PedidoResponseDTO> recentOrders) {
        this.stats = stats;
        this.chartData = chartData;
        this.recentOrders = recentOrders;
    }

    public List<StatCardResponseDTO> getStats() {
        return stats;
    }

    public void setStats(List<StatCardResponseDTO> stats) {
        this.stats = stats;
    }

    public List<ChartDataResponseDTO> getChartData() {
        return chartData;
    }

    public void setChartData(List<ChartDataResponseDTO> chartData) {
        this.chartData = chartData;
    }

    public List<PedidoResponseDTO> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(List<PedidoResponseDTO> recentOrders) {
        this.recentOrders = recentOrders;
    }
}
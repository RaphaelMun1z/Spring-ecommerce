package io.github.raphaelmun1z.ecommerce.dtos.res;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Objeto de resposta consolidado para o dashboard administrativo")
public class DashboardResponseDTO {

    @Schema(description = "Lista de cartões de estatísticas principais (KPIs)")
    private List<StatCardResponseDTO> stats;

    @Schema(description = "Dados para alimentação de gráficos visuais")
    private List<ChartDataResponseDTO> chartData;

    @Schema(description = "Lista simplificada dos pedidos mais recentes")
    private List<PedidoResponseDTO> recentOrders;

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
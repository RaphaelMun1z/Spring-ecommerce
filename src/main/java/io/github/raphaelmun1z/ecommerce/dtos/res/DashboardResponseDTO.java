package io.github.raphaelmun1z.ecommerce.dtos.res;

import java.util.List;

public class DashboardResponseDTO {

    private List<StatCardResponseDTO> stats;
    private List<ChartDataResponseDTO> chartData;
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
package io.github.raphaelmun1z.ecommerce.dtos.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para renderização de gráficos no dashboard")
public class ChartDataResponseDTO {

    @Schema(description = "Rótulo do eixo X (ex: Dia da semana, Mês)", example = "Seg")
    private String label;

    @Schema(description = "Valor percentual ou absoluto para altura da barra no gráfico", example = "40%")
    private String height;

    public ChartDataResponseDTO(String label, String height) {
        this.label = label;
        this.height = height;
    }

    // Getters
    public String getLabel() {
        return label;
    }

    public String getHeight() {
        return height;
    }
}
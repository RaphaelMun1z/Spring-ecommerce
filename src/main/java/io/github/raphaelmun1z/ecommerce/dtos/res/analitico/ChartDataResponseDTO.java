package io.github.raphaelmun1z.ecommerce.dtos.res.analitico;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para dados de gráficos simples")
public class ChartDataResponseDTO {

    @Schema(description = "Rótulo do eixo X (ex: dia da semana)", example = "Seg")
    private String label;

    @Schema(description = "Altura da barra em porcentagem ou valor", example = "75%")
    private String height;

    public ChartDataResponseDTO() {
    }

    public ChartDataResponseDTO(String label, String height) {
        this.label = label;
        this.height = height;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
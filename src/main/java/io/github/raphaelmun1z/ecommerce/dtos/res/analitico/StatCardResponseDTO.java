package io.github.raphaelmun1z.ecommerce.dtos.res.analitico;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para cards estatísticos do dashboard")
public class StatCardResponseDTO {

    @Schema(description = "Título do card", example = "Vendas Totais")
    private String title;

    @Schema(description = "Valor formatado", example = "R$ 1.250,00")
    private String value;

    @Schema(description = "Texto da variação percentual", example = "+15%")
    private String change;

    @Schema(description = "Indica se a variação é positiva (bom) ou negativa (ruim)", example = "true")
    private boolean positive;

    @Schema(description = "Descrição do período comparativo", example = "vs. mês anterior")
    private String period;

    @Schema(description = "Classe de ícone (ex: Phosphor Icons)", example = "ph-currency-dollar")
    private String icon;

    @Schema(description = "Classes CSS para estilização de cores", example = "bg-green-50 text-green-600")
    private String colorClass;

    public StatCardResponseDTO() {
    }

    public StatCardResponseDTO(String title, String value, String change, boolean positive, String period, String icon, String colorClass) {
        this.title = title;
        this.value = value;
        this.change = change;
        this.positive = positive;
        this.period = period;
        this.icon = icon;
        this.colorClass = colorClass;
    }

    // Getters e Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColorClass() {
        return colorClass;
    }

    public void setColorClass(String colorClass) {
        this.colorClass = colorClass;
    }
}
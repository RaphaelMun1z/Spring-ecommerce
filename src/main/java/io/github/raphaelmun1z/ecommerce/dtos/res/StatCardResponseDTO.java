package io.github.raphaelmun1z.ecommerce.dtos.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para representação de cartões de estatísticas (KPIs) no dashboard")
public class StatCardResponseDTO {

    @Schema(description = "Título da métrica", example = "Vendas Totais")
    private String title;

    @Schema(description = "Valor formatado da métrica", example = "R$ 128.450")
    private String value;

    @Schema(description = "Texto indicativo da variação percentual", example = "+12%")
    private String change;

    @Schema(description = "Indica se a variação é positiva (verde) ou negativa (vermelho)", example = "true")
    private boolean isPositive;

    @Schema(description = "Texto explicativo da comparação", example = "vs. mês anterior")
    private String comparisonText;

    @Schema(description = "Identificador do ícone (ex: Phosphor Icons)", example = "ph-currency-dollar")
    private String icon;

    @Schema(description = "Classes CSS para estilização do ícone/fundo", example = "bg-green-50 text-green-600")
    private String colorClass;

    public StatCardResponseDTO(String title, String value, String change, boolean isPositive, String comparisonText, String icon, String colorClass) {
        this.title = title;
        this.value = value;
        this.change = change;
        this.isPositive = isPositive;
        this.comparisonText = comparisonText;
        this.icon = icon;
        this.colorClass = colorClass;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public String getChange() {
        return change;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public String getComparisonText() {
        return comparisonText;
    }

    public String getIcon() {
        return icon;
    }

    public String getColorClass() {
        return colorClass;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }

    public void setComparisonText(String comparisonText) {
        this.comparisonText = comparisonText;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setColorClass(String colorClass) {
        this.colorClass = colorClass;
    }
}
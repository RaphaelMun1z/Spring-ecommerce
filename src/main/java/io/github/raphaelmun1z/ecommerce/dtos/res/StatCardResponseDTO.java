package io.github.raphaelmun1z.ecommerce.dtos.res;

public class StatCardResponseDTO {
    private String title;
    private String value;
    private String change;
    private boolean isPositive;
    private String comparisonText;
    private String icon;
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

    // Setters (Adicionados para corrigir o erro de acesso)
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
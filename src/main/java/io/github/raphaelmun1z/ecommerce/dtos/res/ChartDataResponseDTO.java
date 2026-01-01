package io.github.raphaelmun1z.ecommerce.dtos.res;

public class ChartDataResponseDTO {
    private String label;
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
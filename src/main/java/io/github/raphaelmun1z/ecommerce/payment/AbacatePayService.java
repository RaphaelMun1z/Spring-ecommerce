package io.github.raphaelmun1z.ecommerce.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raphaelmun1z.ecommerce.payment.dto.AbacatePayResponse;
import io.github.raphaelmun1z.ecommerce.payment.dto.CreateBillingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class AbacatePayService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public AbacatePayService(@Value("${abacatepay.api.key}") String apiKey, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.abacatepay.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public AbacatePayResponse criarCobranca(CreateBillingRequest request) {
        try {
            String jsonRequest = objectMapper.writeValueAsString(request);
            System.out.println(">>> REQUEST ABACATEPAY: " + jsonRequest);

            String jsonResponse = restClient.post()
                    .uri("/billing/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonRequest)
                    .retrieve()
                    .body(String.class);

            System.out.println(">>> RESPONSE ABACATEPAY: " + jsonResponse);

            AbacatePayResponse response = objectMapper.readValue(jsonResponse, AbacatePayResponse.class);

            if (response.data() == null) {
                String msgErro = response.error() != null ? response.error() : "Erro desconhecido na AbacatePay";
                throw new RuntimeException("AbacatePay recusou a criação: " + msgErro);
            }

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro interno ao processar JSON do pagamento", e);
        } catch (RestClientResponseException e) {
            System.err.println("Erro HTTP API AbacatePay: " + e.getResponseBodyAsString());
            throw new RuntimeException("Falha na comunicação com AbacatePay: " + e.getStatusText());
        } catch (Exception e) {
            if (e.getMessage().contains("AbacatePay recusou")) {
                throw new RuntimeException(e.getMessage());
            }
            throw new RuntimeException("Erro inesperado no gateway de pagamento", e);
        }
    }
}
package io.github.raphaelmun1z.ecommerce.controllers.analitico;

import io.github.raphaelmun1z.ecommerce.controllers.analitico.docs.RelatorioControllerDocs;
import io.github.raphaelmun1z.ecommerce.services.analitico.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController implements RelatorioControllerDocs {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @GetMapping("/vendas")
    public ResponseEntity<byte[]> baixarRelatorioVendas(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        // Se não informar datas, assume o mês atual
        LocalDateTime dataInicio = (inicio != null) ? inicio.atStartOfDay() : LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime dataFim = (fim != null) ? fim.atTime(LocalTime.MAX) : LocalDateTime.now();

        byte[] relatorio = service.gerarRelatorioVendas(dataInicio, dataFim);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-vendas.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(relatorio);
    }

    @GetMapping("/estoque")
    public ResponseEntity<byte[]> baixarRelatorioEstoque() {
        byte[] relatorio = service.gerarRelatorioEstoque();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-estoque.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(relatorio);
    }
}
package io.github.raphaelmun1z.ecommerce.controllers.analitico;

import io.github.raphaelmun1z.ecommerce.dtos.res.DashboardResponseDTO;
import io.github.raphaelmun1z.ecommerce.services.analitico.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/visao-geral")
    public ResponseEntity<DashboardResponseDTO> getVisaoGeral() {
        DashboardResponseDTO dto = service.getVisaoGeral();
        return ResponseEntity.ok(dto);
    }
}
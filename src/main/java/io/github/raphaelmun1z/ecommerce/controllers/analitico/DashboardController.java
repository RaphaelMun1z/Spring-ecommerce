package io.github.raphaelmun1z.ecommerce.controllers.analitico;

import io.github.raphaelmun1z.ecommerce.controllers.analitico.docs.DashboardControllerDocs;
import io.github.raphaelmun1z.ecommerce.dtos.res.analitico.DashboardResponseDTO;
import io.github.raphaelmun1z.ecommerce.services.analitico.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController implements DashboardControllerDocs {
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
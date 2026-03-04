package io.github.raphaelmun1z.ecommerce.controllers.storage;

import io.github.raphaelmun1z.ecommerce.services.storage.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/arquivos")
@Tag(name = "Upload de Arquivos", description = "Gerenciamento de imagens via Cloudinary")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Fazer upload de uma imagem", description = "Envia para o Cloudinary e retorna a URL pública.")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String urlDaImagem = fileStorageService.storeFile(file);
        return ResponseEntity.ok(Map.of(
                "url", urlDaImagem
        ));
    }

}
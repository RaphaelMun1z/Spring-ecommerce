package io.github.raphaelmun1z.ecommerce.controllers.storage;

import io.github.raphaelmun1z.ecommerce.services.storage.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/arquivos")
@Tag(name = "Upload de Arquivos", description = "Gerenciamento de imagens e documentos")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Fazer upload de um arquivo", description = "Retorna a URL pública para acesso.")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        // Gera a URL completa para download/visualização
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/arquivos/download/")
            .path(fileName)
            .toUriString();

        return ResponseEntity.ok(Map.of(
            "fileName", fileName,
            "url", fileDownloadUri,
            "type", file.getContentType(),
            "size", String.valueOf(file.getSize())
        ));
    }

    @GetMapping("/download/{fileName:.+}")
    @Operation(summary = "Baixar/Visualizar arquivo")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Tenta determinar o tipo de arquivo
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Logger info could go here
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
}
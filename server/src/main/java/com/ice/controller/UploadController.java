package com.ice.controller;

import com.ice.dto.upload.UploadImageResponse;
import com.ice.service.LocalUploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/uploads")
public class UploadController {

    private final LocalUploadService localUploadService;

    public UploadController(LocalUploadService localUploadService) {
        this.localUploadService = localUploadService;
    }

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadImageResponse uploadImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        return localUploadService.storeImage(file, request);
    }

    @GetMapping("/files/**")
    public ResponseEntity<Resource> serveFile(HttpServletRequest request) {
        String fullPath = request.getRequestURI();
        String prefix = "/api/v1/uploads/files/";
        int idx = fullPath.indexOf(prefix);
        if (idx < 0) {
            return ResponseEntity.notFound().build();
        }
        String relative = fullPath.substring(idx + prefix.length());
        Resource resource = localUploadService.loadAsResource(relative);
        MediaType mediaType = localUploadService.mediaTypeFor(relative);
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                .contentType(mediaType)
                .body(resource);
    }
}

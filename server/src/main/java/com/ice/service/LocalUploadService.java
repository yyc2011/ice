package com.ice.service;

import com.ice.config.UploadProperties;
import com.ice.dto.upload.UploadImageResponse;
import com.ice.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalUploadService {

    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "webp");

    private final UploadProperties uploadProperties;

    public LocalUploadService(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    public UploadImageResponse storeImage(MultipartFile file, HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("VALIDATION_ERROR", "请选择图片文件", HttpStatus.BAD_REQUEST);
        }
        long maxBytes = uploadProperties.getMaxSizeMb() * 1024L * 1024L;
        if (file.getSize() > maxBytes) {
            throw new ApiException(
                    "VALIDATION_ERROR",
                    "图片不能超过 " + uploadProperties.getMaxSizeMb() + "MB",
                    HttpStatus.BAD_REQUEST
            );
        }
        String ext = resolveExtension(file.getOriginalFilename(), file.getContentType());
        if (!ALLOWED_EXT.contains(ext)) {
            throw new ApiException("VALIDATION_ERROR", "仅支持 jpg/png/webp", HttpStatus.BAD_REQUEST);
        }

        LocalDate today = LocalDate.now();
        String year = today.format(DateTimeFormatter.ofPattern("yyyy"));
        String month = today.format(DateTimeFormatter.ofPattern("MM"));
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path relative = Paths.get(year, month, filename);
        Path root = Paths.get(uploadProperties.getLocalDir()).toAbsolutePath().normalize();
        Path target = root.resolve(relative).normalize();
        if (!target.startsWith(root)) {
            throw new ApiException("VALIDATION_ERROR", "非法路径", HttpStatus.BAD_REQUEST);
        }
        try {
            Files.createDirectories(target.getParent());
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target);
            }
        } catch (IOException ex) {
            throw new ApiException("UPLOAD_FAILED", "保存图片失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String publicPath = uploadProperties.getPublicBasePath() + "/" + year + "/" + month + "/" + filename;
        String absoluteUrl = buildAbsoluteUrl(request, publicPath);
        return new UploadImageResponse(absoluteUrl);
    }

    public Resource loadAsResource(String relativePath) {
        Path root = Paths.get(uploadProperties.getLocalDir()).toAbsolutePath().normalize();
        Path target = root.resolve(relativePath).normalize();
        if (!target.startsWith(root) || !Files.isRegularFile(target)) {
            throw new ApiException("NOT_FOUND", "文件不存在", HttpStatus.NOT_FOUND);
        }
        return new FileSystemResource(target);
    }

    public MediaType mediaTypeFor(String relativePath) {
        String lower = relativePath.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (lower.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        }
        return MediaType.IMAGE_JPEG;
    }

    private static String resolveExtension(String filename, String contentType) {
        if (filename != null && filename.contains(".")) {
            String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
            if (ALLOWED_EXT.contains(ext)) {
                return "jpeg".equals(ext) ? "jpg" : ext;
            }
        }
        if (contentType != null) {
            return switch (contentType.toLowerCase(Locale.ROOT)) {
                case "image/png" -> "png";
                case "image/webp" -> "webp";
                case "image/jpeg", "image/jpg" -> "jpg";
                default -> "";
            };
        }
        return "";
    }

    private static String buildAbsoluteUrl(HttpServletRequest request, String publicPath) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        boolean defaultPort = ("http".equals(scheme) && port == 80)
                || ("https".equals(scheme) && port == 443);
        StringBuilder sb = new StringBuilder();
        sb.append(scheme).append("://").append(host);
        if (!defaultPort) {
            sb.append(':').append(port);
        }
        sb.append(publicPath);
        return sb.toString();
    }
}

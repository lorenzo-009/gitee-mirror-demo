package com.lut.grade.util;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 文件上传工具。
 * 头像不保存在 webapp 下 —— 否则每次重新部署都会被清空，
 * 统一落到用户目录，再由 FileServlet 读取回显。
 */
public final class UploadUtil {

    private static final Path AVATAR_DIR =
            Paths.get(System.getProperty("user.home"), "sms-data", "avatar");

    public static final long MAX_SIZE = 2 * 1024 * 1024L;

    private UploadUtil() {
    }

    public static Path avatarDir() {
        try {
            Files.createDirectories(AVATAR_DIR);
        } catch (IOException e) {
            throw new IllegalStateException("无法创建头像目录：" + AVATAR_DIR, e);
        }
        return AVATAR_DIR;
    }

    /**
     * 保存上传的头像，返回服务器上的文件名。
     * 文件名用 UUID 重排，避免中文名与覆盖问题。
     */
    public static String saveAvatar(Part part) throws IOException {
        String submitted = part.getSubmittedFileName();
        String ext = "";
        if (submitted != null && submitted.contains(".")) {
            ext = submitted.substring(submitted.lastIndexOf('.'));
        }
        if (!ext.matches("(?i)\\.(jpg|jpeg|png|gif|bmp)")) {
            ext = ".png";
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
        Path target = avatarDir().resolve(fileName);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }

    public static Path resolveAvatar(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }
        return avatarDir().resolve(fileName).normalize();
    }
}

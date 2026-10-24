package com.lut.grade.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 密码工具：随机盐 + MD5。
 * 作业要求允许 MD5；生产环境建议升级为 BCrypt / PBKDF2。
 */
public final class MD5Util {

    private static final char[] HEX = "0123456789abcdef".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    private MD5Util() {
    }

    /** 生成 16 位随机盐。 */
    public static String randomSalt() {
        byte[] buf = new byte[8];
        RANDOM.nextBytes(buf);
        return hex(buf);
    }

    /** 加盐 MD5，返回值固定 32 位小写十六进制。 */
    public static String encrypt(String plainPassword, String salt) {
        return md5(md5(plainPassword) + salt);
    }

    public static boolean matches(String plainPassword, String salt, String stored) {
        return encrypt(plainPassword, salt).equals(stored);
    }

    private static String md5(String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return hex(digest.digest(source.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("当前环境不支持 MD5 算法", e);
        }
    }

    private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX[(b >> 4) & 0x0F]).append(HEX[b & 0x0F]);
        }
        return sb.toString();
    }
}

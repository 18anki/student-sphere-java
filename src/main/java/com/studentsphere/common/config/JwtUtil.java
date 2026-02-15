package com.studentsphere.common.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long jwtRefreshExpirationMs;

    private final ObjectMapper mapper = new ObjectMapper();

    private static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private static byte[] base64UrlDecode(String s) {
        return Base64.getUrlDecoder().decode(s);
    }

    private String sign(String headerPayload) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(keySpec);
            byte[] sig = hmac.doFinal(headerPayload.getBytes(StandardCharsets.UTF_8));
            return base64UrlEncode(sig);
        } catch (Exception e) {
            throw new RuntimeException("Error signing JWT", e);
        }
    }

    public String generateAccessToken(Long userId, String fullName) {
        try {
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            long now = System.currentTimeMillis();
            Map<String, Object> payload = new HashMap<>();
            payload.put("sub", String.valueOf(userId));
            payload.put("name", fullName);
            payload.put("iat", now / 1000);
            payload.put("exp", (now + jwtExpirationMs) / 1000);

            String headerJson = mapper.writeValueAsString(header);
            String payloadJson = mapper.writeValueAsString(payload);

            String headerB64 = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
            String payloadB64 = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

            String headerPayload = headerB64 + "." + payloadB64;
            String signature = sign(headerPayload);
            return headerPayload + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Unable to generate access token", e);
        }
    }

    public String generateRefreshToken(Long userId) {
        try {
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            long now = System.currentTimeMillis();
            Map<String, Object> payload = new HashMap<>();
            payload.put("sub", String.valueOf(userId));
            payload.put("iat", now / 1000);
            payload.put("exp", (now + jwtRefreshExpirationMs) / 1000);

            String headerJson = mapper.writeValueAsString(header);
            String payloadJson = mapper.writeValueAsString(payload);

            String headerB64 = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
            String payloadB64 = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

            String headerPayload = headerB64 + "." + payloadB64;
            String signature = sign(headerPayload);
            return headerPayload + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Unable to generate refresh token", e);
        }
    }

    public Map<String, Object> getClaimsFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new RuntimeException("Invalid JWT token format");
            String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
            return mapper.readValue(payloadJson, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse token claims", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;
            String headerPayload = parts[0] + "." + parts[1];
            String signature = parts[2];
            String expected = sign(headerPayload);
            if (!expected.equals(signature)) return false;

            Map<String, Object> claims = getClaimsFromToken(token);
            long exp = ((Number) claims.get("exp")).longValue();
            long nowSec = System.currentTimeMillis() / 1000;
            return nowSec < exp;
        } catch (Exception e) {
            return false;
        }
    }
}

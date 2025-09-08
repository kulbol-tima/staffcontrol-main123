package kg.mlsp.staffcontrol.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final byte[] KEY = "1234567896543210".getBytes(); // 16 байт для AES-128
    private static final byte[] IV = "123456789012".getBytes();      // 12 байт для GCM

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, IV);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
            byte[] encrypted = cipher.doFinal(attribute.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        
        // Проверяем, является ли данные Base64-закодированными (зашифрованными)
        if (!isBase64Encoded(dbData)) {
            // Если данные не зашифрованы, возвращаем как есть
            return dbData;
        }
        
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, IV);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(dbData));
            return new String(decrypted);
        } catch (Exception e) {
            // Если не удалось расшифровать, возможно данные не зашифрованы
            // Возвращаем исходные данные
            return dbData;
        }
    }
    
    private boolean isBase64Encoded(String str) {
        try {
            Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

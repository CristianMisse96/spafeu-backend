package edu.co.udes.spafeu.web.app.config;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtKeyConfig {

    @Bean
    SecretKey jwtSecretKey(
            @Value("${jwt.secret:}") String encodedSecret) {

        if (encodedSecret == null || encodedSecret.isBlank()) {
            throw new IllegalStateException(
                "La propiedad jwt.secret es obligatoria. "
                + "Configura la variable de entorno JWT_SECRET."
            );
        }

        final byte[] keyBytes;

        try {
            keyBytes = Base64.getDecoder().decode(encodedSecret);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "JWT_SECRET debe contener una clave codificada en Base64.",
                e
            );
        }

        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                "JWT_SECRET debe contener al menos 32 bytes "
                + "después de decodificar Base64 para HS256."
            );
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }
}

package by.skachkovdmitry.personal_account.core.utils;

import by.dmitryskachkov.entity.VerificationError;
import by.skachkovdmitry.personal_account.config.properties.JWTProperty;
import by.skachkovdmitry.personal_account.core.dto.security.UserSecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenHandler {

    private final JWTProperty property;

    private final ObjectMapper objectMapper;


    public JwtTokenHandler(JWTProperty property, ObjectMapper objectMapper) {
        this.property = property;
        this.objectMapper = objectMapper;
    }

    public String generateAccessToken(UserSecurity userSecurity) {
        try {
            String token = Jwts.builder()
                    .setSubject(objectMapper.writeValueAsString(userSecurity))
                    .setIssuer(property.getIssuer())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7))) // 1 week
                    .signWith(SignatureAlgorithm.HS512, property.getSecret())
                    .compact();
            addToContext(userSecurity);
            return token;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private void addToContext(UserSecurity userSecurity) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userSecurity, null, userSecurity.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    public UserSecurity getUser(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(property.getSecret())
                .parseClaimsJws(token)
                .getBody();
        try {
            return objectMapper.readValue(claims.getSubject(), UserSecurity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(property.getSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(property.getSecret()).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new VerificationError("ошибка доступа1");
            //logger.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            throw new VerificationError("ошибка доступа2");
            //logger.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            throw new VerificationError("ошибка доступа3");
            //logger.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            throw new VerificationError("ошибка доступа4");
            //logger.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new VerificationError("ошибка доступа5");
            //logger.error("JWT claims string is empty - {}", ex.getMessage());
        }
    }
}

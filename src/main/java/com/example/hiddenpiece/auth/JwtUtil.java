package com.example.hiddenpiece.auth;

import com.example.hiddenpiece.exception.CustomException;
import com.example.hiddenpiece.exception.CustomExceptionCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    public static final long ACCESS_EXPIRE_TIME = 60 * 60 * 1000;
    public static final long REFRESH_EXPIRE_TIME = 60 * 60 * 1000 * 6;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final Key key;
    private final JwtParser jwtParser;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.key).build();
    }

    public boolean validate(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            log.info("validate success");
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(CustomExceptionCode.EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(CustomExceptionCode.UNSUPPORTED_JWT);
        } catch (SignatureException | MalformedJwtException e) {
            throw new CustomException(CustomExceptionCode.INVALID_JWT);
        } catch (IllegalArgumentException e) {
            throw new CustomException(CustomExceptionCode.ILLEGAL_ARGUMENT_JWT);
        }
    }

    public Claims parseClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String generateToken(String username, long time) {
        Claims jwtClaims = Jwts.claims()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time));

        return Jwts.builder().setClaims(jwtClaims).signWith(key).compact();
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

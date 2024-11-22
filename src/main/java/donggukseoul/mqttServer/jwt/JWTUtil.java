package donggukseoul.mqttServer.jwt;

import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.service.TokenBlacklistService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;


    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {


        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String validateToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, TokenBlacklistService tokenBlacklistService) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return null;
//            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String token = authorization.split(" ")[1];

//        if (isExpired(token)) {
//            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
//        }
//
//        if (tokenBlacklistService.isTokenBlacklisted(token)) {
//            throw new CustomException(ErrorCode.TOKEN_BLACKLISTED);
//        }

        return token;
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String username, String role, Long expiredMs) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiredMs);

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }
}
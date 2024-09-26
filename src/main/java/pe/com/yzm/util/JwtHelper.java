package pe.com.yzm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pe.com.yzm.core.exception.BusinessException;
import pe.com.yzm.model.User;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
public class JwtHelper {

  @Value("${jwt.secret}")
  private String jwtSecret;


  public Mono<String> createToken(User user) {
    return Mono.fromCallable(() -> {
      final var now = new Date();
      final var experationDate = new Date(now.getTime() + (3600 * 1000));
      return Jwts.builder()
          .setSubject(user.getEmail())
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(experationDate)
          .signWith(getSecretKey())
          .claim("role", user.getRoleId())
          .claim("id", user.getId())
          .claim("name", user.getName())
          .claim("email", user.getEmail())
          .compact();
    });
  }

  public Mono<Boolean> validateToken(String token) {
    return getExpirationDate(token)
        .map(expirationDate -> expirationDate.after(new Date()))
        .doOnError(error -> log.error(ConstantMessage.JWT_INCORRECT, error))
        .onErrorResume(error -> Mono.error(new BusinessException(ConstantMessage.USER_EXCEPTION, List.of( ConstantMessage.JWT_INCORRECT), HttpStatus.UNAUTHORIZED)));
  }

  private Mono<Date> getExpirationDate(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  private <T> Mono<T> getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    return Mono.fromCallable(() -> claimsResolver.apply(signToken(token)));
  }

  private Claims signToken(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }
}

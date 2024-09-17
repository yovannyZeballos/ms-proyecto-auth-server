package pe.com.yzm.business;

import pe.com.yzm.core.model.HeaderRequest;
import pe.com.yzm.expose.request.UserCreateRequest;
import pe.com.yzm.expose.response.TokenResponse;
import pe.com.yzm.expose.request.UserRequest;
import reactor.core.publisher.Mono;

public interface AuthService {
  Mono<TokenResponse> login(HeaderRequest headerRequest, UserRequest userResquest);

  Mono<TokenResponse> create(HeaderRequest headerRequest, UserCreateRequest userRequest);

  Mono<TokenResponse> validateToken(HeaderRequest headerRequest, TokenResponse token);
}

package pe.com.yzm.expose;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.com.yzm.business.AuthService;
import pe.com.yzm.core.model.HeaderRequest;
import pe.com.yzm.core.model.Wrapper;
import pe.com.yzm.expose.request.UserCreateRequest;
import pe.com.yzm.expose.request.UserRequest;
import pe.com.yzm.expose.response.TokenResponse;
import pe.com.yzm.util.ConstantHeader;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
public class AuthController {

  private final AuthService authService;

  @PostMapping(path = "/login")
  public Mono<TokenResponse> jwtCreate(
      @RequestHeader(value = ConstantHeader.TRANSACTION_ID) String idTransaction,
      @Valid @RequestBody UserRequest userRequest) {

    var headerRequest = HeaderRequest.builder()
        .transactionId(idTransaction)
        .build();

    return authService.login(headerRequest, userRequest);
  }

  @PostMapping(path = "/register")
  public Mono<TokenResponse> register(
      @RequestHeader(value = ConstantHeader.TRANSACTION_ID) String idTransaction,
      @Valid @RequestBody UserCreateRequest userRequest) {

    var headerRequest = HeaderRequest.builder()
        .transactionId(idTransaction)
        .build();

    return authService.create(headerRequest, userRequest);
  }

  @PostMapping(path = "/jwt")
  public Mono<TokenResponse> jwtValidate(
      @RequestHeader(value = ConstantHeader.TRANSACTION_ID) String idTransaction,
      @RequestHeader String accessToken) {

    var headerRequest = HeaderRequest.builder()
        .transactionId(idTransaction)
        .build();

    return authService.validateToken(headerRequest,
            TokenResponse.builder()
                .accessToken(accessToken)
                .build());
  }

}

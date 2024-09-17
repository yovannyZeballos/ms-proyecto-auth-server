package pe.com.yzm.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.com.yzm.business.AuthService;
import pe.com.yzm.core.exception.BusinessException;
import pe.com.yzm.core.logger.LoggerUtil;
import pe.com.yzm.core.model.HeaderRequest;
import pe.com.yzm.expose.request.UserCreateRequest;
import pe.com.yzm.expose.request.UserRequest;
import pe.com.yzm.expose.response.TokenResponse;
import pe.com.yzm.mapper.UserMapper;
import pe.com.yzm.repository.UserRepository;
import pe.com.yzm.model.User;
import pe.com.yzm.util.ConstantMessage;
import pe.com.yzm.util.JwtHelper;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtHelper jwtHelper;
  private final UserMapper userMapper;


  @Override
  public Mono<TokenResponse> login(HeaderRequest headerRequest, UserRequest userResponse) {
    LoggerUtil.logInput(headerRequest.getTransactionId(), headerRequest.toString(), userResponse);

    return userRepository.findByEmail(userResponse.getEmail())
        .switchIfEmpty(Mono.error(buildBusinessException(ConstantMessage.USER_ICORRECT)))
        .filterWhen(user -> validPassword(userResponse, user))
        .switchIfEmpty(Mono.error(buildBusinessException(ConstantMessage.USER_ICORRECT)))
        .flatMap(user -> jwtHelper.createToken(user)
            .map(token -> TokenResponse.builder().accessToken(token).build()))
        .doOnNext(response ->
            LoggerUtil.logOutput(headerRequest.getTransactionId(), headerRequest.toString(), response.toString()))
        .doOnError(error -> log.error(ConstantMessage.USER_EXCEPTION, error));
  }

  @Override
  public Mono<TokenResponse> create(HeaderRequest headerRequest, UserCreateRequest userRequest) {
    LoggerUtil.logInput(headerRequest.getTransactionId(), headerRequest.toString(), userRequest);
    return validateSaveUser(userRequest)
        .map(userMapper::userCreateRequestToUser)
        .flatMap(user -> {
          user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
          return userRepository.save(user);
        })
        .flatMap(user -> jwtHelper.createToken(user)
            .map(token -> TokenResponse.builder().accessToken(token).build()))
        .doOnNext(response ->
            LoggerUtil.logOutput(headerRequest.getTransactionId(), headerRequest.toString(), response.toString()))
        .doOnError(error -> log.error(ConstantMessage.USER_EXCEPTION, error));
  }

  @Override
  public Mono<TokenResponse> validateToken(HeaderRequest headerRequest, TokenResponse token) {
    LoggerUtil.logInput(headerRequest.getTransactionId(), headerRequest.toString(), token);

    return jwtHelper.validateToken(token.getAccessToken())
        .filter(valid -> valid)
        .switchIfEmpty(Mono.error(buildBusinessException(ConstantMessage.JWT_INCORRECT)))
        .thenReturn(token)
        .doOnNext(response ->
            LoggerUtil.logOutput(headerRequest.getTransactionId(), headerRequest.toString(), response.toString()));
  }

  private Mono<Boolean> validPassword(UserRequest userRequest, User user) {
    return Mono.fromCallable(() -> passwordEncoder.matches(userRequest.getPassword(), user.getPassword()));
  }

  private BusinessException buildBusinessException(String detail) {
    return BusinessException.builder()
        .message(ConstantMessage.USER_EXCEPTION)
        .details(List.of(ConstantMessage.USER_ICORRECT))
        .httpStatus(HttpStatus.UNAUTHORIZED)
        .build();
  }

  private Mono<UserCreateRequest> validateSaveUser(UserCreateRequest userRequest) {
    return userRepository.existsByEmail(userRequest.getEmail())
        .flatMap(exist -> exist ? Mono.error(BusinessException.createException(ConstantMessage.ERROR_SAVE_USER,
            List.of(String.format(ConstantMessage.USER_EXIST, userRequest.getEmail())), HttpStatus.BAD_REQUEST)) : Mono.just(userRequest));
  }

}

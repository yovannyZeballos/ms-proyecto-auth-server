package pe.com.yzm.expose.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(exclude = {"password"})
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class UserCreateRequest {

  @NotBlank
  @Email
  String email;

  @NotBlank
  String password;

  @NotBlank
  String name;

}

package pe.com.yzm.expose.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class TokenResponse {
    private String accessToken;
}

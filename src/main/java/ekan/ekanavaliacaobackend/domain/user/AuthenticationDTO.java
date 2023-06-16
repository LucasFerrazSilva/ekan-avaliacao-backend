package ekan.ekanavaliacaobackend.domain.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
public class AuthenticationDTO {

    @NotBlank
    private String login;
    @NotBlank
    private String password;

}

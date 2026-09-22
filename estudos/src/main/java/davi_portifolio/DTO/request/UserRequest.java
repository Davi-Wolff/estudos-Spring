package davi_portifolio.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Username é obrigatório")
    private String username;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

}

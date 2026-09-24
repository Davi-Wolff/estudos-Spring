package davi_portifolio.exception.custom;

import jakarta.validation.constraints.NotBlank;

public class UsernameAlreadyExistsException extends Throwable {
    public UsernameAlreadyExistsException(@NotBlank(message = "Username é obrigatório") String username) {
    }
}

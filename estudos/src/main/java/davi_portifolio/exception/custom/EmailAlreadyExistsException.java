package davi_portifolio.exception.custom;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailAlreadyExistsException extends Throwable {
    public EmailAlreadyExistsException(@Email(message = "Email inválido") @NotBlank(message = "Email é obrigatório") String email) {
    }
}

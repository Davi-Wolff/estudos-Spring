package davi_portifolio.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record UserRequest(
        @NotBlank String username,
        @Email @NotBlank String email,
        String password
) {}

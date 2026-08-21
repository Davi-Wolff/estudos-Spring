package davi_portifolio.DTO.response;

public record UserDTO(
        Long id,
        String username,
        String email
) {}

//lembrar de nunca passar a senha pelo dto

package davi_portifolio.DTO.request;

public record UserCreateRequest(String username,
                                String email,
                                String hashedPassword) {

}

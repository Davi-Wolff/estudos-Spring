package davi_portifolio.DTO;

public record UserDTO( Long id,
         String email,
         String hashedPassword,
         String name,
         long phone) {
}

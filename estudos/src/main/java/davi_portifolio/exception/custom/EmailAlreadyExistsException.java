package davi_portifolio.exception.custom;


public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email já está em uso: " + email);
    }
}

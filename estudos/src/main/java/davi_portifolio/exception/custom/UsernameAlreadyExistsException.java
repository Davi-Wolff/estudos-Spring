package davi_portifolio.exception.custom;


public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Username já está em uso: " + username);
    }
}
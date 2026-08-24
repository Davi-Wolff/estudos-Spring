package davi_portifolio.service;

import davi_portifolio.DTO.request.UserCreateRequest;
import davi_portifolio.DTO.response.UserDTO;
import davi_portifolio.entity.User;
import davi_portifolio.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        User user = userRepository.findById(id).get();
        return user;
    }

    public User buscarPorEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    public User createUser(UserCreateRequest request){
        User novoUser = new User();
        novoUser.setUsername(request.username());
        novoUser.setEmail(request.email());
        novoUser.setHashedPassword(request.hashedPassword());
        userRepository.save(novoUser);
        return novoUser;
    }

    public UserDTO toDTO(User novoUser){
        return new UserDTO(novoUser.getId(),novoUser.getUsername(),novoUser.getEmail());
    }

    //fzr exceptions para case de errado e ajustar o controller caso a busca dê errado
}

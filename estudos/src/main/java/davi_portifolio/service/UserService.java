package davi_portifolio.service;

import davi_portifolio.DTO.request.UserCreateRequest;
import davi_portifolio.DTO.request.UserRequest;
import davi_portifolio.DTO.response.UserDTO;
import davi_portifolio.entity.Role;
import davi_portifolio.entity.User;
import davi_portifolio.exception.custom.EmailAlreadyExistsException;
import davi_portifolio.exception.custom.UsernameAlreadyExistsException;
import davi_portifolio.repository.UserRepository;
import davi_portifolio.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public User findById(Long id) throws UsernameNotFoundException{
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Usuário não encontrado"));

    }

    public User findByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Usuário não encontrado"));
    }

    public User createUser(UserCreateRequest request) throws EmailAlreadyExistsException, UsernameAlreadyExistsException {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }

        User novoUser = new User();
        novoUser.setUsername(request.username());
        novoUser.setEmail(request.email());
        novoUser.setPassword(request.password());
        novoUser.setRole(Role.ROLE_USER);

        User saved = userRepository.save(novoUser);

        emailService.sendEmail(
                saved.getEmail(),
                "Bem-vindo!",
                "Olá, " + saved.getUsername() + "! Sua conta foi criada com sucesso."
        );

        return saved;
    }

    public User updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setUpdated_at(new Date());

        return userRepository.save(user);
    }

    public boolean deleteUser(Long id){
        userRepository.deleteById(id);
        return true;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}

package davi_portifolio.service;

import davi_portifolio.DTO.request.UserCreateRequest;
import davi_portifolio.DTO.request.UserRequest;
import davi_portifolio.DTO.response.UserDTO;
import davi_portifolio.entity.Role;
import davi_portifolio.entity.User;
import davi_portifolio.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public User createUser(UserCreateRequest request){
        User novoUser = new User();
        novoUser.setUsername(request.username());
        novoUser.setEmail(request.email());
        novoUser.setPassword(request.hashedPassword());
        novoUser.setRole(Role.ROLE_USER);
        userRepository.save(novoUser);
        return novoUser;
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

package davi_portifolio.controller;

import davi_portifolio.DTO.request.UserCreateRequest;
import davi_portifolio.DTO.request.UserRequest;
import davi_portifolio.DTO.response.UserDTO;
import davi_portifolio.entity.User;
import davi_portifolio.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

//Fazer um CRUD de User pra pegar o jeito primeiro

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toDTO(user));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> findUserByUsername(@PathVariable String username){
        User user = userService.findByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toDTO(user));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email){
        User user = userService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        User novoUsuario = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.toDTO(novoUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserRequest request, @PathVariable Long id) {
        User updatedUser = userService.updateUser(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@Valid @PathVariable Long id) {
        boolean deuCerto = userService.deleteUser(id);
        if(deuCerto) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }else  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

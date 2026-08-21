package davi_portifolio.controller;

import davi_portifolio.DTO.request.UserCreateRequest;
import davi_portifolio.DTO.response.UserDTO;
import davi_portifolio.entity.User;
import davi_portifolio.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> buscarPerfilId(@PathVariable Long id) {
        User user = userService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> criarUser(@RequestBody UserCreateRequest request) {
        User novoUsuario = userService.criarUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.toDTO(novoUsuario));
    }

}

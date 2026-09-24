package davi_portifolio.controller;

import davi_portifolio.DTO.request.UserCreateRequest;
import davi_portifolio.DTO.request.UserRequest;
import davi_portifolio.DTO.response.UserDTO;
import davi_portifolio.entity.User;
import davi_portifolio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "Controller for the user")
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Operation(summary = "Get user by Id", description = "User must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Id supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toDTO(user));
    }

    @Operation(summary = "Get user by username", description = "User must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> findUserByUsername(@PathVariable String username){
        User user = userService.findByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toDTO(user));
    }


    @Operation(summary = "Get all users")
    @GetMapping("/findall")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Operation(summary = "Get user by email", description = "User must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/get/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email){
        User user = userService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toDTO(user));
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))} ),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email or username already in use"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        //consertar createUser
        User novoUsuario = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.toDTO(novoUsuario));
    }

    @Operation(summary = "Udate a user", description = "User must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserRequest request, @PathVariable Long id) {
        User updatedUser = userService.updateUser(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toDTO(updatedUser));
    }

    @Operation(summary = "Delete a user", description = "User must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserDTO> deleteUser(@Valid @PathVariable Long id) {
        boolean deuCerto = userService.deleteUser(id);
        if(deuCerto) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }else  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

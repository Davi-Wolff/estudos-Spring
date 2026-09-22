package davi_portifolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import davi_portifolio.dto.UserCreateRequest;
import davi_portifolio.dto.UserDTO;
import davi_portifolio.dto.UserRequest;
import davi_portifolio.entity.User;
import davi_portifolio.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de UserController com @WebMvcTest (sobe só a camada web, UserService é mockado).
 *
 * Assunções feitas (ajuste se divergir do projeto real):
 *  - @RequestMapping("/users") na classe do controller
 *  - Spring Security habilitado -> @WithMockUser para autenticar as requisições
 *  - Nenhum @ControllerAdvice tratando UsernameNotFoundException -> vira 500 por padrão
 *
 * IMPORTANTE: como estão hoje, findUserById/findUserByUsername/findUserByEmail usam o
 * mesmo padrão de path ("/{algumaCoisa}"), o que é uma AMBIGUOUS MAPPING para o Spring.
 * Na prática, a primeira requisição real para qualquer um desses três endpoints lança
 * IllegalStateException. Corrija os paths (ex: /id/{id}, /username/{username},
 * /email/{email}) antes de rodar os testes abaixo.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String BASE_URL = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User criarUsuarioFake() {
        User user = new User(1L, "davi@email.com", "hash123", "davi", new Date(), null);
        user.setId(1L);
        return user;
    }

    // ---------- GET /{id} ----------

    @Test
    @WithMockUser
    void findUserById_deveRetornar200ComUsuario() throws Exception {
        User user = criarUsuarioFake();
        UserDTO dto = new UserDTO(1L, "davi", "davi@email.com");

        when(userService.findById(1L)).thenReturn(user);
        when(userService.toDTO(user)).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("davi"))
                .andExpect(jsonPath("$.email").value("davi@email.com"));
    }

    @Test
    @WithMockUser
    void findUserById_deveRetornarErroServidor_quandoNaoExistir() throws Exception {
        when(userService.findById(99L)).thenThrow(new UsernameNotFoundException("User not found"));

        // Sem @ControllerAdvice mapeando essa exceção para 404, o Spring devolve 500.
        mockMvc.perform(get(BASE_URL + "/{id}", 99L))
                .andExpect(status().is5xxServerError());
    }

    // ---------- POST ----------

    @Test
    @WithMockUser
    void createUser_deveRetornar201ComUsuarioCriado() throws Exception {
        UserCreateRequest request = new UserCreateRequest("davi", "davi@email.com", "hash123");
        User userSalvo = criarUsuarioFake();
        UserDTO dto = new UserDTO(1L, "davi", "davi@email.com");

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(userSalvo);
        when(userService.toDTO(userSalvo)).thenReturn(dto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("davi"));
    }

    @Test
    @WithMockUser
    void createUser_deveRetornar400_quandoRequestInvalido() throws Exception {
        // corpo vazio deve falhar nas validações de @Valid em UserCreateRequest
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ---------- PUT ----------

    @Test
    @WithMockUser
    void updateUser_deveRetornar200ComUsuarioAtualizado() throws Exception {
        UserRequest request = new UserRequest("novoNome", "novo@email.com");
        User userAtualizado = criarUsuarioFake();
        UserDTO dto = new UserDTO(1L, "davi", "davi@email.com");

        when(userService.updateUser(eq(1L), any(UserRequest.class))).thenReturn(userAtualizado);
        when(userService.toDTO(userAtualizado)).thenReturn(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ---------- DELETE ----------
    // OBS: no UserService real, deleteUser() sempre retorna true (deleteById não
    // verifica existência), então o branch 404 abaixo nunca é exercitado em produção.
    // O teste cobre o comportamento do controller assumindo que o service pudesse
    // retornar false.

    @Test
    @WithMockUser
    void deleteUser_deveRetornar200_quandoSucesso() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteUser_deveRetornar404_quandoServiceRetornaFalse() throws Exception {
        when(userService.deleteUser(99L)).thenReturn(false);

        mockMvc.perform(delete(BASE_URL + "/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
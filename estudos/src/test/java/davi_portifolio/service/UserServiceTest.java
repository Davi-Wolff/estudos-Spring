package davi_portifolio.service;

import davi_portifolio.DTO.request.UserCreateRequest;
import davi_portifolio.DTO.response.UserDTO;
import davi_portifolio.DTO.request.UserRequest;
import davi_portifolio.entity.Role;
import davi_portifolio.entity.User;
import davi_portifolio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários de UserService, isolando o repositório com Mockito.
 *
 * Assunções feitas (ajuste se divergir do projeto real):
 *  - UserDTO(Long id, String username, String email) é um record
 *  - UserCreateRequest(String username, String email, String hashedPassword) é um record
 *  - UserRequest(String username, String email) é um record (não usado hoje pelo service)
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    private User criarUsuario(Long id, String email, String password, String username) {
        User user = new User(id, email, password, username, new Date(), null, Role.ROLE_USER);
        user.setId(id); // necessário: o construtor de User não seta o id
        return user;
    }

    // ---------- findById ----------

    @Test
    void findById_deveRetornarUsuario_quandoExistir() {
        User user = criarUsuario(1L, "davi@email.com", "hash123", "davi");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User resultado = userService.findById(1L);

        assertThat(resultado).isEqualTo(user);
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_deveLancarExcecao_quandoNaoExistir() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

    // ---------- findByEmail ----------

    @Test
    void findByEmail_deveRetornarUsuario_quandoExistir() {
        User user = criarUsuario(1L, "davi@email.com", "hash123", "davi");
        when(userRepository.findByEmail("davi@email.com")).thenReturn(Optional.of(user));

        User resultado = userService.findByEmail("davi@email.com");

        assertThat(resultado.getEmail()).isEqualTo("davi@email.com");
    }

    @Test
    void findByEmail_deveLancarExcecao_quandoNaoExistir() {
        when(userRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail("naoexiste@email.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    // ---------- findByUsername ----------

    @Test
    void findByUsername_deveRetornarUsuario_quandoExistir() {
        User user = criarUsuario(1L, "davi@email.com", "hash123", "davi");
        when(userRepository.findByUsername("davi")).thenReturn(Optional.of(user));

        User resultado = userService.findByUsername("davi");

        assertThat(resultado.getUsername()).isEqualTo("davi");
    }

    @Test
    void findByUsername_deveLancarExcecao_quandoNaoExistir() {
        when(userRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername("fantasma"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    // ---------- createUser ----------

    @Test
    void createUser_deveCriarESalvarNovoUsuario() {
        UserCreateRequest request = new UserCreateRequest("davi", "davi@email.com", "hash123");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        User resultado = userService.createUser(request);

        verify(userRepository).save(captor.capture());
        User usuarioSalvo = captor.getValue();
        assertThat(usuarioSalvo.getUsername()).isEqualTo("davi");
        assertThat(usuarioSalvo.getEmail()).isEqualTo("davi@email.com");
        assertThat(usuarioSalvo.getPassword()).isEqualTo("hash123");
        assertThat(resultado).isSameAs(usuarioSalvo);
    }

    // ---------- updateUser ----------
    // ATENÇÃO: os testes abaixo documentam o comportamento ATUAL do método,
    // que (1) ignora completamente os dados de "request" e (2) não preserva
    // o id do usuário (o construtor de User não seta o campo id, então o
    // save() vira um INSERT em vez de UPDATE). Ajuste estes testes depois de
    // corrigir o UserService.

    @Test
    void updateUser_deveLancarExcecao_quandoUsuarioNaoExistir() {
        UserRequest request = new UserRequest("novoNome", "novo@email.com", "hash123");
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(99L, request))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuário não encontrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_comportamentoAtual_mantemDadosAntigosENaoPreservaId() {
        User antigoUser = criarUsuario(1L, "antigo@email.com", "hashAntigo", "antigoNome");
        UserRequest request = new UserRequest("novoNome", "novo@email.com", "hash123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(antigoUser));

        User resultado = userService.updateUser(1L, request);

        // BUG: mantém email/username antigos porque "request" nunca é lido
        assertThat(resultado.getEmail()).isEqualTo("antigo@email.com");
        assertThat(resultado.getUsername()).isEqualTo("antigoNome");
        // BUG: id fica null porque o construtor de User não seta o id
        assertThat(resultado.getId()).isNull();
        verify(userRepository).save(resultado);
    }

    // ---------- deleteUser ----------

    @Test
    void deleteUser_deveChamarDeleteByIdERetornarTrue() {
        boolean resultado = userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
        assertThat(resultado).isTrue();
    }

    // ---------- toDTO ----------

    @Test
    void toDTO_deveConverterUserParaUserDTO() {
        User user = criarUsuario(1L, "davi@email.com", "hash123", "davi");

        UserDTO dto = userService.toDTO(user);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.username()).isEqualTo("davi");
        assertThat(dto.email()).isEqualTo("davi@email.com");
    }
}
package davi_portifolio.repository;

import davi_portifolio.entity.Role;
import davi_portifolio.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de UserRepository com um Postgres real via Testcontainers (evita
 * discrepâncias de comportamento entre H2 e Postgres em produção).
 *
 * ATENÇÃO: a classe User fornecida NÃO tem um setRole(), mas a coluna
 * "user_role" é NOT NULL. O helper criarUsuario() abaixo assume que você vai
 * adicionar esse setter (ou um construtor que aceite Role) — sem isso, nenhum
 * save() real funciona.
 *
 * Requer no pom/build.gradle: org.testcontainers:postgresql e
 * org.testcontainers:junit-jupiter.
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configurarDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    private User criarUsuario(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("hash123");
        user.setCreated_at(new Date());
        user.setRole(Role.ROLE_USER); // ver aviso na descrição da classe
        return user;
    }

    @Test
    void deveSalvarERecuperarUsuarioPorId() {
        User salvo = userRepository.save(criarUsuario("davi", "davi@email.com"));

        Optional<User> encontrado = userRepository.findById(salvo.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getUsername()).isEqualTo("davi");
    }

    @Test
    void deveEncontrarUsuarioPorEmail() {
        userRepository.save(criarUsuario("davi", "davi@email.com"));

        Optional<User> encontrado = userRepository.findByEmail("davi@email.com");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getUsername()).isEqualTo("davi");
    }

    @Test
    void deveRetornarVazio_quandoEmailNaoExistir() {
        Optional<User> encontrado = userRepository.findByEmail("naoexiste@email.com");

        assertThat(encontrado).isEmpty();
    }

    @Test
    void deveEncontrarUsuarioPorUsername() {
        userRepository.save(criarUsuario("davi", "davi@email.com"));

        Optional<User> encontrado = userRepository.findByUsername("davi");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo("davi@email.com");
    }

    @Test
    void deveRetornarVazio_quandoUsernameNaoExistir() {
        Optional<User> encontrado = userRepository.findByUsername("fantasma");

        assertThat(encontrado).isEmpty();
    }

    @Test
    void deveRespeitarUnicidadeDeUsername() {
        userRepository.saveAndFlush(criarUsuario("davi", "davi1@email.com"));

        User duplicado = criarUsuario("davi", "davi2@email.com");

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(duplicado));
    }

    @Test
    void deveRemoverUsuarioPorId() {
        User salvo = userRepository.saveAndFlush(criarUsuario("davi", "davi@email.com"));

        userRepository.deleteById(salvo.getId());

        assertThat(userRepository.findById(salvo.getId())).isEmpty();
    }
}
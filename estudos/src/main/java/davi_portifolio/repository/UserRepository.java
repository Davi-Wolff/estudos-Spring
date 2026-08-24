package davi_portifolio.repository;

import davi_portifolio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User WHERE u.email = :email")
    public User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User WHERE u.username = :username")
    public User findByUsername(@Param("username") String username);

}

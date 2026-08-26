package davi_portifolio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "user_email", nullable = false, updatable = true)
    private String email;

    @Column(name = "user_hashedPassword", nullable = false, updatable = true, unique = false)
    private String hashedPassword;

    @Column(name = "user_username", nullable = false, updatable = true, unique = true)
    private String username;

    @Column(name = "created_at", nullable = false, updatable = false, unique = false)
    private Date created_at;

    @Column(name = "updated_at", nullable = true, updatable = true, unique = false)
    private Date updated_at;




    public User() {
    }

    public User(Long id, String email, String hashedPassword, String username, Date created_at,Date updated_at) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.username = username;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

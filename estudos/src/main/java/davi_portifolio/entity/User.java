package davi_portifolio.entity;

import jakarta.persistence.*;

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

    @Column(name = "user_phone", nullable = true, updatable = true, unique = true)
    private Long phone;


    public User() {
    }

    public User(Long id, String email, String hashedPassword, String username, long phone) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.username = username;
        this.phone = phone;
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

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
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

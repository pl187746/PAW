package pl.iis.paw.trello.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

@Entity
public class User {

    @GeneratedValue
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Email
    @Column(name = "email")
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities = new HashSet<>();
    
    @OneToMany(mappedBy = "user", targetEntity = FavBoard.class, cascade = CascadeType.REMOVE)
    private List<FavBoard> favoriteBoards;

    public User() { } // JPA

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public List<FavBoard> getFavoriteBoards() {
		return favoriteBoards;
	}

	public void setFavoriteBoards(List<FavBoard> favoriteBoards) {
		this.favoriteBoards = favoriteBoards;
	}

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
            Objects.equals(login, user.login) &&
            Objects.equals(password, user.password) &&
            Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email);
    }
}

package pl.iis.paw.trello.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import java.util.List;

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
    
    @ManyToMany(mappedBy = "likingUsers")
    @JoinTable(name = "UsersFavoriteBoards",
		joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") },
		inverseJoinColumns = { @JoinColumn(name = "board_id", referencedColumnName = "board_id") }
    	)
    @JsonIgnore
    private List<Board> favoriteBoards;

    public User() { } // JPA

    public User(String login, String password, String email, List<Board> favoriteBoards) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.favoriteBoards = favoriteBoards;
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

	public List<Board> getFavoriteBoards() {
		return favoriteBoards;
	}

	public void setFavoriteBoards(List<Board> favoriteBoards) {
		this.favoriteBoards = favoriteBoards;
	}
}

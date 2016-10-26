package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Board implements Serializable {
	
	private static final long serialVersionUID = -7062787189193439539L;

	@GeneratedValue
    @Id
    @Column(name = "board_id")
    private Long id;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(mappedBy = "board", targetEntity = CardList.class, cascade = CascadeType.ALL)
	private List<CardList> lists;
	
	@ManyToMany(mappedBy = "favoriteBoards")
	@JoinTable(name = "UsersFavoriteBoards",
		joinColumns = { @JoinColumn(name = "board_id", referencedColumnName = "board_id") },
		inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") }
		)
	@JsonIgnore
	private List<User> likingUsers;
	
	public Board() { }

	public Board(String name, List<CardList> lists) {
		super();
		this.name = name;
		this.lists = lists;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CardList> getLists() {
		return lists;
	}

	public void setLists(List<CardList> lists) {
		this.lists = lists;
	}

	public List<User> getLikingUsers() {
		return likingUsers;
	}

	public void setLikingUsers(List<User> likingUsers) {
		this.likingUsers = likingUsers;
	}

}

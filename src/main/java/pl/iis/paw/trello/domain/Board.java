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

	@OneToMany(mappedBy = "board", targetEntity = FavBoard.class, cascade = CascadeType.REMOVE)
    private List<FavBoard> likingUsers;

	@ManyToMany(targetEntity = User.class)
	private List<User> members;

	@OneToMany(mappedBy = "board", targetEntity = Record.class, cascade = CascadeType.REMOVE)
	private List<Record> diary;

	@OneToMany(mappedBy = "board", targetEntity = Label.class, cascade = CascadeType.REMOVE)
	private List<Label> availableLabels;

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

	public List<FavBoard> getLikingUsers() {
		return likingUsers;
	}

	public void setLikingUsers(List<FavBoard> likingUsers) {
		this.likingUsers = likingUsers;
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public List<Record> getDiary() {
		return diary;
	}

	public void setDiary(List<Record> diary) {
		this.diary = diary;
	}

	public List<Label> getAvailableLabels() {
		return availableLabels;
	}

	public void setAvailableLabels(List<Label> availableLabels) {
		this.availableLabels = availableLabels;
	}

}

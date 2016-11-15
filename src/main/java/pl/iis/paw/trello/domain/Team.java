package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

@Entity
public class Team implements Serializable {

	private static final long serialVersionUID = -551729226321276472L;

	@GeneratedValue
    @Id
    @Column(name = "team_id")
    private Long id;

	@Column(name = "name")
	private String name;

	@ManyToMany(targetEntity = User.class)
	@JoinTable(
		joinColumns = { @JoinColumn(name = "team_id", referencedColumnName = "team_id") },
		inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") },
		uniqueConstraints = { @UniqueConstraint(columnNames = { "team_id", "user_id" }) }
	)
	private List<User> users;

	@OneToMany(targetEntity = Board.class, mappedBy = "team")
	private List<Board> boards;

	public Team() { }

	public Team(String name) {
		super();
		this.name = name;
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Board> getBoards() {
		return boards;
	}

	public void setBoards(List<Board> boards) {
		this.boards = boards;
	}

}

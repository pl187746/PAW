package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	@JoinTable(
		joinColumns = { @JoinColumn(name = "board_id", referencedColumnName = "board_id") },
		inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") },
		uniqueConstraints = { @UniqueConstraint(columnNames = { "board_id", "user_id" }) }
	)
	private List<User> members;

	@ManyToOne(targetEntity = Team.class, optional = true)
	@JoinColumn(name = "team_id", referencedColumnName = "team_id")
	@JsonIgnore
	private Team team;

	@OneToMany(mappedBy = "board", targetEntity = Record.class, cascade = CascadeType.REMOVE)
	private List<Record> diary;

	@OneToMany(mappedBy = "board", targetEntity = Label.class, cascade = CascadeType.REMOVE)
	private List<Label> availableLabels;

	@ManyToMany(targetEntity = User.class)
	@JoinTable(name = "board_subscribers",
		joinColumns = {@JoinColumn(name = "board_id", referencedColumnName = "board_id")},
		inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")})
	@JsonIgnore
	private List<User> subscribers;

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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public List<User> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<User> subscribers) {
		this.subscribers = subscribers;
	}

	@JsonProperty("teamId")
	public Long getTeamId() {
		return Optional.ofNullable(team)
				.map(Team::getId)
				.orElse(null);
	}

	@JsonProperty("teamId")
	public void setTeamId(Long teamId) {
		this.team = new Team();
		this.team.setId(teamId);
	}


	@JsonProperty(value = "subscribers", access = JsonProperty.Access.READ_ONLY)
	public List<String> getSubsrcribersLogins() {
		return subscribers.stream().map(sub -> sub.getLogin()).collect(Collectors.toList());
	}
}

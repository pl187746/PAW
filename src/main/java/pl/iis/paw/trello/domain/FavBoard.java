package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "board_id" }) })
public class FavBoard implements Serializable {

	private static final long serialVersionUID = -26974227330791246L;

	@GeneratedValue
    @Id
    @Column(name = "favboard_id")
    private Long id;
	
	@ManyToOne(targetEntity = User.class, optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	@JsonIgnore
	private User user;
	
	@ManyToOne(targetEntity = Board.class, optional = false)
	@JoinColumn(name = "board_id", referencedColumnName = "board_id")
	@JsonIgnore
	private Board board;
	
	public FavBoard() { }

	public FavBoard(User user, Board board) {
		super();
		this.user = user;
		this.board = board;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	@JsonProperty(value = "userId")
	public void setUserId(Long userId) {
		this.user = new User();
		this.user.setId(userId);
	}
	
	@JsonProperty(value = "userId")
	public Long getUserId() {
		return Optional.ofNullable(user)
				.map(User::getId)
				.orElse(null);
	}
	
	@JsonProperty(value = "boardId")
	public void setBoardId(Long boardId) {
		this.board = new Board();
		this.board.setId(boardId);
	}
	
	@JsonProperty(value = "boardId")
	public Long getBoardId() {
		return Optional.ofNullable(board)
				.map(Board::getId)
				.orElse(null);
	}
	
}

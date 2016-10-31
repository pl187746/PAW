package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Record implements Serializable {
	
	private static final long serialVersionUID = 6084774886805852838L;

	@GeneratedValue
    @Id
    @Column(name = "record_id")
    private Long id;
	
	@Enumerated(EnumType.STRING)
	private RecordType type;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date timestamp;
	
	@ManyToOne(targetEntity = Board.class)
	@JoinColumn(name = "board_id", referencedColumnName = "board_id")
	@JsonIgnore
	private Board board;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	@JsonIgnore
	private User user;
	
	@ElementCollection
	private Map<String, String> params;
	
	public Record() { }

	public Record(RecordType type, Date timestamp, Board board, User user, Map<String, String> params) {
		super();
		this.type = type;
		this.timestamp = timestamp;
		this.board = board;
		this.user = user;
		this.params = params;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RecordType getType() {
		return type;
	}

	public void setType(RecordType type) {
		this.type = type;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
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

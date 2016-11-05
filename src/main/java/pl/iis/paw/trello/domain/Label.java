package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Label implements Serializable {

	private static final long serialVersionUID = 5249406438394345867L;

	@GeneratedValue
    @Id
    @Column(name = "label_id")
    private Long id;

	@ManyToOne(targetEntity = Board.class, optional = false)
	@JoinColumn(name = "board_id", referencedColumnName = "board_id")
	@JsonIgnore
	private Board board;

	@Column(name = "name")
	private String name;

	@Column(name = "color")
	private String color;

	public Label() { }

	public Label(Board board, String name, String color) {
		super();
		this.board = board;
		this.name = name;
		this.color = color;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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

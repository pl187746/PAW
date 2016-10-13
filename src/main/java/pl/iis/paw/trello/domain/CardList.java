package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CardList implements Serializable {
	
	private static final long serialVersionUID = 5895531407173648L;

	@GeneratedValue
    @Id
    @Column(name = "list_id")
    private Long id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(targetEntity = Board.class, optional = false)
	@JoinColumn(name = "board_id", referencedColumnName = "board_id")
	@JsonIgnore
	private Board board;
	
	@OneToMany(mappedBy = "cardList", targetEntity = Card.class, cascade = CascadeType.REMOVE)
	private List<Card> cards;
	
	public CardList() { }

	public CardList(String name, Board board, List<Card> cards) {
		super();
		this.name = name;
		this.board = board;
		this.cards = cards;
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

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

}

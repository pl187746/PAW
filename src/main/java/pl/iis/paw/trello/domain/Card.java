package pl.iis.paw.trello.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Card implements Serializable {

	private static final long serialVersionUID = 8114319009607882193L;

	@GeneratedValue
    @Id
    @Column(name = "card_id")
    private Long id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "list_id", referencedColumnName = "list_id")
	private CardList cardList;
	
	public Card() { }

	public Card(Long id, String name, CardList cardList) {
		super();
		this.id = id;
		this.name = name;
		this.cardList = cardList;
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

	public CardList getCardList() {
		return cardList;
	}

	public void setCardList(CardList cardList) {
		this.cardList = cardList;
	}
}
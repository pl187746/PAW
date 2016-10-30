package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	@JsonIgnore
	private CardList cardList;
	
	@Column(name = "card_ord")
	private Long ord;
	
	@Column(name = "archive")
	private boolean archive;
	
	public Card() { }

	public Card(String name, CardList cardList) {
		super();
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

	@JsonProperty(value = "listId")
	public void setListId(Long listId) {
		this.cardList = new CardList();
		this.cardList.setId(listId);
	}
	
	@JsonProperty(value = "listId")
	public Long getListId() {
		return Optional.ofNullable(cardList)
				.map(CardList::getId)
				.orElse(null);
	}

	public Long getOrd() {
		return ord;
	}

	public void setOrd(Long ord) {
		this.ord = ord;
	}
	
	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

}

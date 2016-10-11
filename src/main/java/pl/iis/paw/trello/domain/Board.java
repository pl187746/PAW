package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Board implements Serializable {
	
	private static final long serialVersionUID = -7062787189193439539L;

	@GeneratedValue
    @Id
    @Column(name = "board_id")
    private Long id;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(mappedBy = "board", targetEntity = CardList.class)
	private List<CardList> lists;
	
	public Board() { }

	public Board(Long id, String name, List<CardList> lists) {
		super();
		this.id = id;
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

}

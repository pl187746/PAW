package pl.iis.paw.trello.domain;

import java.io.Serializable;

public class BoardInfo implements Serializable {
	
	private static final long serialVersionUID = 3552259790467792405L;

	private Long id;
	
	private String name;
	
	public BoardInfo() {}

	public BoardInfo(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public BoardInfo(Board board) {
		this(board.getId(), board.getName());
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
	
	public Board toBoard() {
		return new Board(this);
	}

}

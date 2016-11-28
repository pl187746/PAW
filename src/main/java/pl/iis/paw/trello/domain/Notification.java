package pl.iis.paw.trello.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Notification implements Serializable {

	private static final long serialVersionUID = -972697738592161479L;

	@GeneratedValue
	@Id
	@Column(name = "notification_id")
	private Long id;

	@ManyToOne(targetEntity = Record.class)
	@JoinColumn(name = "record_id", referencedColumnName = "record_id")
	private Record record;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	@JsonIgnore
	private User user;

	public Notification() {
	}

	public Notification(Record record, User user) {
		super();
		this.record = record;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

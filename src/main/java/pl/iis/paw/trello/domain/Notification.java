package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

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

@Entity
public class Notification implements Serializable {

	private static final long serialVersionUID = -7921500368064598647L;

	@GeneratedValue
	@Id
	@Column(name = "notification_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private RecordType type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date timestamp;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "performing_user_id", referencedColumnName = "user_id")
	@JsonIgnore
	private User performingUser;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "notified_user_id", referencedColumnName = "user_id")
	@JsonIgnore
	private User notifiedUser;

	@ElementCollection
	private Map<String, String> params;

	public Notification() {
	}

	public Notification(RecordType type, Date timestamp, User performingUser, User notifiedUser,
			Map<String, String> params) {
		super();
		this.type = type;
		this.timestamp = timestamp;
		this.performingUser = performingUser;
		this.notifiedUser = notifiedUser;
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

	public User getPerformingUser() {
		return performingUser;
	}

	public void setPerformingUser(User performingUser) {
		this.performingUser = performingUser;
	}

	public User getNotifiedUser() {
		return notifiedUser;
	}

	public void setNotifiedUser(User notifiedUser) {
		this.notifiedUser = notifiedUser;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

}

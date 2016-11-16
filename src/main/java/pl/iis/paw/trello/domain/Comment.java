package pl.iis.paw.trello.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Krystian on 2016-11-05.
 */
@Entity
public class Comment implements Serializable {

    private static final long serialVersionUID = 3795667771158313900L;

    @GeneratedValue
    @Id
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(targetEntity = Card.class)
    @JoinColumn(name = "card_id", referencedColumnName = "card_id")
    @JsonIgnore
    private Card card;

    @OneToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    @JsonIgnore
    private User author;

    @Column(name = "date")
    private Date date;

    public Comment() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @JsonProperty(value = "cardId")
    public void setCardId(Long cardId) {
        this.card = new Card();
        this.card.setId(cardId);
    }

    @JsonProperty(value = "cardId")
    public Long getCardId() {
        return Optional.ofNullable(card)
                .map(Card::getId)
                .orElse(null);
    }

    @JsonProperty(value = "author", access = JsonProperty.Access.READ_ONLY)
    public String getAuthorLogin() {
        return author.getLogin();
    }
}

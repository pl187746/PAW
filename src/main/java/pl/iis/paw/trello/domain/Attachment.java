package pl.iis.paw.trello.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Attachment {

    @Id
    @GeneratedValue
    @Column(name = "attachment_id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "card_id")
    @JsonIgnore
    private Card card;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    @JsonIgnore
    private User author;

    public Attachment() { }

    public Attachment(String fileName, Card card, User author) {
        this.fileName = fileName;
        this.card = card;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @JsonProperty(value = "cardId", access = JsonProperty.Access.READ_ONLY)
    public Long getCardId() {
        return card.getId();
    }

    @JsonProperty(value = "author", access = JsonProperty.Access.READ_ONLY)
    public String getAuthorLogin() {
        return author.getLogin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, card);
    }

    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + id +
            ", fileName='" + fileName + '\'' +
            ", card=" + card +
            '}';
    }
}

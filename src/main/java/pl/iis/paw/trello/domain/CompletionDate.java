package pl.iis.paw.trello.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class CompletionDate {

    @Id
    @GeneratedValue
    @Column(name = "completion_id")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @OneToOne(mappedBy = "completionDate")
    @JsonIgnore
    private Card card;

    @Column(name = "finished")
    private Boolean finished;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @JsonProperty(value = "cardId")
    public Long getCardId() {
        return card.getId();
    }

    @JsonProperty(value = "cardId")
    public void setCardId(Long cardId) {
        this.card.setId(cardId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletionDate that = (CompletionDate) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(card, that.card) &&
            Objects.equals(finished, that.finished);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, card, finished);
    }

    @Override
    public String toString() {
        return "CompletionDate{" +
            "id=" + id +
            ", date=" + date +
            ", card=" + card +
            ", finished=" + finished +
            '}';
    }
}

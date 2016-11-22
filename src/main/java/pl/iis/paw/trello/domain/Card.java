package pl.iis.paw.trello.domain;

import java.io.Serializable;
import java.util.List;
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

    @Column(name = "archive")
    private boolean archive;

    @ManyToOne
    @JoinColumn(name = "list_id", referencedColumnName = "list_id")
    @JsonIgnore
    private CardList cardList;

    @Column(name = "card_ord")
    private Long ord;

    @ManyToMany(targetEntity = Label.class)
    private List<Label> labels;

    @OneToMany(mappedBy = "card", targetEntity = Comment.class)
    private List<Comment> comments;

    @OneToMany(mappedBy = "card", targetEntity = Attachment.class, cascade = CascadeType.ALL)
    private List<Attachment> attachments;

    @OneToOne(targetEntity = CompletionDate.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "completion_id", referencedColumnName = "completion_id")
    private CompletionDate completionDate;

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

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public CompletionDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(CompletionDate completionDate) {
        this.completionDate = completionDate;
    }
}

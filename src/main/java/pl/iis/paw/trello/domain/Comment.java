package pl.iis.paw.trello.domain;


import javax.persistence.Entity;

/**
 * Created by Shogun on 03.11.2016.
 */

public class Comment {

    private String comment;

    public Comment(String comment) {
        this.comment = comment;
    }

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

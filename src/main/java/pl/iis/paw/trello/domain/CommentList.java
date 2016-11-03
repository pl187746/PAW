package pl.iis.paw.trello.domain;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shogun on 03.11.2016.
 */


public class CommentList {
    private List<Comment> comments = new ArrayList<>();

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }
}

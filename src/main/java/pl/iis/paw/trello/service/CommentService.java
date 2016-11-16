package pl.iis.paw.trello.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.domain.Comment;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.exception.CommentNotFoundException;
import pl.iis.paw.trello.repository.CommentRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Krystian on 2016-11-05.
 */
@Service
public class CommentService {

    private final static Logger log = LoggerFactory.getLogger(CommentService.class);

    private CommentRepository commentRepository;

    private UserService userService;
    private CardService cardService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, CardService cardService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.cardService = cardService;
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getComments(Pageable pageable) {
        return commentRepository.findAll(pageable).getContent();
    }

    public Comment findCommentById(Long commentId) {
        return Optional
                .ofNullable(commentRepository.findOne(commentId))
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    public Comment addComment(Long cardId, String content,Date date) {
        User currentUser = userService.getCurrentUser();
        Card card = cardService.findCardById(cardId);

        Comment comment = new Comment();
        comment.setAuthor(currentUser);
        comment.setContent(content);
        comment.setCard(card);
        comment.setDate(date);

        return commentRepository.save(comment);
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(Comment comment) {
        return updateComment(comment.getId(),comment);
    }

    public Comment updateComment(Long commentId, Comment comment) {
        Comment existingComment = findCommentById(commentId);

        existingComment.setContent(comment.getContent());

        return commentRepository.save(existingComment);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    public void deleteComment(Long commentId) {
        deleteComment(findCommentById(commentId));
    }
}
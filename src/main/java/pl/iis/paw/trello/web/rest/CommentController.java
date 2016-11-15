package pl.iis.paw.trello.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.domain.Comment;
import pl.iis.paw.trello.service.CardService;
import pl.iis.paw.trello.service.CommentService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Krystian on 2016-11-05.
 */
@RestController
public class CommentController {

    private CommentService commentService;
    private CardService cardService;

    @Autowired
    public CommentController(CommentService commentService,CardService cardService) {
        this.commentService = commentService;
        this.cardService = cardService;
    }

    @RequestMapping(value = "/comments",method = RequestMethod.GET)
    public ResponseEntity<?> getComments(Pageable pageable){
        return ResponseEntity.ok(commentService.getComments(pageable));
    }

    @RequestMapping(value = "/comments/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getComment(@PathVariable(value = "id")Long commentId){
        return ResponseEntity.ok(commentService.findCommentById(commentId));
    }

    @RequestMapping(value = "/comments",method = RequestMethod.POST)
    public ResponseEntity<?> createComment(@Valid @RequestBody Comment comment) throws URISyntaxException {
        Card card = cardService.findCardById(comment.getCard().getId());
        comment.setCard(card);

        return ResponseEntity
                .created(new URI("/comments/" + comment.getId()))
                .body(commentService.addComment(comment));
    }

    @RequestMapping(value = "/comments",method = RequestMethod.PUT)
    public ResponseEntity<?> updateComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.updateComment(comment));
    }

    @RequestMapping(value = "/comments/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@PathVariable(value = "id")Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
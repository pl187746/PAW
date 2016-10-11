package pl.iis.paw.trello.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.service.BoardService;

@RestController
public class BoardController {
	
	private BoardService boardService;
	
	@Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
	
	@RequestMapping(value = "/boards", method = RequestMethod.GET)
    public ResponseEntity<?> getBoards(Pageable pageable) {
        return ResponseEntity.ok(boardService.getBoards(pageable));
    }
	
	@RequestMapping(value = "/boards/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getBoard(@PathVariable(value = "id") Long boardId) {
        return ResponseEntity.ok(boardService.findBoardById(boardId));
    }

    @RequestMapping(value = "/boards", method = RequestMethod.POST)
    public ResponseEntity<?> createBoard(@Valid @RequestBody Board board) throws URISyntaxException {
        return ResponseEntity
            .created(new URI("/boards/" + board.getId()))
            .body(boardService.addBoard(board));
    }

    @RequestMapping(value = "/boards", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBoard(@RequestBody Board board) {
        return ResponseEntity.ok(boardService.updateBoard(board));
    }

    @RequestMapping(value = "/boards/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok().build();
    }

}

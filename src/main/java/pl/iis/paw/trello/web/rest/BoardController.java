package pl.iis.paw.trello.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getBoards(Pageable pageable,@RequestParam(value = "share",required = false) String shareLink) {
	    if(StringUtils.isEmpty(shareLink)){
            return ResponseEntity.ok(boardService.getBoards(pageable));
        } else {
            return ResponseEntity.ok(boardService.findBoardByShareUrl(shareLink));
        }
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

    @RequestMapping(value = "/boards/{boardId}/subscribe", method = RequestMethod.POST)
    public ResponseEntity<?> subscribeBoard(@PathVariable Long boardId) {
        boardService.subscribeBoard(boardId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/boards/{boardId}/unsubscribe", method = RequestMethod.POST)
    public ResponseEntity<?> unsubscribeBoard(@PathVariable Long boardId) {
        boardService.unsubscribeBoard(boardId);
        return ResponseEntity.ok().build();
    }
}

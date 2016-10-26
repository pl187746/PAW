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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.FavBoard;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.service.BoardService;
import pl.iis.paw.trello.service.FavBoardService;
import pl.iis.paw.trello.service.UserService;

@RestController
public class FavBoardController {
	
	private FavBoardService favBoardService;
	private UserService userService;
	private BoardService boardService;

	@Autowired
	public FavBoardController(FavBoardService favBoardService, UserService userService, BoardService boardService) {
		super();
		this.favBoardService = favBoardService;
		this.userService = userService;
		this.boardService = boardService;
	}
	
	@RequestMapping(value = "/favboards", method = RequestMethod.GET)
	public ResponseEntity<?> getFavBoards(Pageable pageable) {
		return ResponseEntity.ok(favBoardService.getFavBoards(pageable));
	}
	
	@RequestMapping(value = "/favboards/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFavBoardd(@PathVariable(value = "id") Long favBoardId) {
		return ResponseEntity.ok(favBoardService.getFavBoardById(favBoardId));
	}
	
	@RequestMapping(value = "/favboards", params = { "userId" }, method = RequestMethod.GET)
	public ResponseEntity<?> getFavBoardsByUserId(@RequestParam("userId") Long userId) {
		User user = userService.findUserById(userId);
		return ResponseEntity.ok(favBoardService.getFavBoardsByUser(user));
	}
	
	@RequestMapping(value = "/favboards", params = { "boardId" }, method = RequestMethod.GET)
	public ResponseEntity<?> getFavBoardsByBoardId(@RequestParam("boardId") Long boardId) {
		Board board = boardService.findBoardById(boardId);
		return ResponseEntity.ok(favBoardService.getFavBoardsByBoard(board));
	}
	
	@RequestMapping(value = "/favboards", params = { "userId", "boardId" }, method = RequestMethod.GET)
	public ResponseEntity<?> getFavBoardByUserIdAndBoardId(@RequestParam("userId") Long userId, @RequestParam("boardId") Long boardId) {
		User user = userService.findUserById(userId);
		Board board = boardService.findBoardById(boardId);
		return ResponseEntity.ok(favBoardService.getFavBoardByUserAndBoard(user, board));
	}
	
	@RequestMapping(value = "/favboards", method = RequestMethod.POST)
    public ResponseEntity<?> createFavBoard(@Valid @RequestBody FavBoard favBoard) throws URISyntaxException {
        return ResponseEntity
            .created(new URI("/favboards/" + favBoard.getId()))
            .body(favBoardService.addFavBoard(favBoard));
    }
	
	@RequestMapping(value = "/favboards/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        favBoardService.deleteFavBoard(id);
        return ResponseEntity.ok().build();
    }

}

package pl.iis.paw.trello.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.CardList;
import pl.iis.paw.trello.service.BoardService;
import pl.iis.paw.trello.service.CardListService;

@RestController
public class CardListController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private BoardService boardService;

	private CardListService cardListService;
	
	@Autowired
    public CardListController(CardListService cardListService, BoardService boardService) {
        this.boardService = boardService;
        this.cardListService = cardListService;
    }
	
	@RequestMapping(value = "/lists", method = RequestMethod.GET)
    public ResponseEntity<?> getCardLists(Pageable pageable) {
        return ResponseEntity.ok(cardListService.getCardLists(pageable));
    }
	
	@RequestMapping(value = "/lists/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCardList(@PathVariable(value = "id") Long cardListId) {
        return ResponseEntity.ok(cardListService.findCardListById(cardListId));
    }

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    public ResponseEntity<?> createCardList(@Valid @RequestBody CardList cardList) throws URISyntaxException {
        log.info("Creating list with name {}", cardList.getName());

        Board board = boardService.findBoardById(cardList.getBoard().getId());
        cardList.setBoard(board);
        cardList.setCards(new ArrayList<>());

        return ResponseEntity
            .created(new URI("/lists/" + cardList.getId()))
            .body(cardListService.addCardList(cardList));
    }

    @RequestMapping(value = "/lists", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCardList(@RequestBody CardList cardList) {
        log.info("Updating list with id {}", cardList.getId());
        return ResponseEntity.ok(cardListService.updateCardList(cardList));
    }

    @RequestMapping(value = "/lists/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCardList(@PathVariable Long id) {
        log.info("Removing list with id {}", id);

        cardListService.deleteCardList(id);
        return ResponseEntity.ok().build();
    }

}

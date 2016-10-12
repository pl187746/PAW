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

import pl.iis.paw.trello.domain.CardList;
import pl.iis.paw.trello.service.CardListService;

@RestController
public class CardListController {

	private CardListService cardListService;
	
	@Autowired
    public CardListController(CardListService cardListService) {
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
        return ResponseEntity
            .created(new URI("/lists/" + cardList.getId()))
            .body(cardListService.addCardList(cardList));
    }

    @RequestMapping(value = "/lists", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCardList(@RequestBody CardList cardList) {
        return ResponseEntity.ok(cardListService.updateCardList(cardList));
    }

    @RequestMapping(value = "/lists/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCardList(@PathVariable Long id) {
        cardListService.deleteCardList(id);
        return ResponseEntity.ok().build();
    }

}

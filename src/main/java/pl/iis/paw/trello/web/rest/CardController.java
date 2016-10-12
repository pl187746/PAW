package pl.iis.paw.trello.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

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

import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.service.CardService;

@RestController
public class CardController {

    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private CardService cardService;
	
	@Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
	
	@RequestMapping(value = "/cards", method = RequestMethod.GET)
    public ResponseEntity<?> getCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getCards(pageable));
    }
	
	@RequestMapping(value = "/cards/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCard(@PathVariable(value = "id") Long cardId) {
        return ResponseEntity.ok(cardService.findCardById(cardId));
    }

    @RequestMapping(value = "/cards", method = RequestMethod.POST)
    public ResponseEntity<?> createCard(@Valid @RequestBody Card card) throws URISyntaxException {
        log.info("Creating card with name" + card.getName());
        return ResponseEntity
            .created(new URI("/cards/" + card.getId()))
            .body(cardService.addCard(card));
    }

    @RequestMapping(value = "/cards", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCard(@RequestBody Card card) {
        log.info("Updating card with id" + card.getId());
        return ResponseEntity.ok(cardService.updateCard(card));
    }

    @RequestMapping(value = "/cards/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCard(@PathVariable Long id) {
        log.info("Removing card with id" + id);
        cardService.deleteCard(id);
        return ResponseEntity.ok().build();
    }

}

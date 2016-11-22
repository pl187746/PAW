package pl.iis.paw.trello.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import pl.iis.paw.trello.domain.Attachment;
import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.domain.CardList;
import pl.iis.paw.trello.domain.CompletionDate;
import pl.iis.paw.trello.service.AttachmentService;
import pl.iis.paw.trello.service.CardListService;
import pl.iis.paw.trello.service.CardService;
import pl.iis.paw.trello.service.StorageService;

@RestController
public class CardController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private CardService cardService;

    private CardListService cardListService;

    private StorageService storageService;

    private AttachmentService attachmentService;

    @Autowired
    public CardController(CardService cardService, CardListService cardListService, StorageService storageService, AttachmentService attachmentService) {
        this.cardService = cardService;
        this.cardListService = cardListService;
        this.storageService = storageService;
        this.attachmentService = attachmentService;
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

        CardList cardList = cardListService.findCardListById(card.getCardList().getId());
        card.setCardList(cardList);

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

    @RequestMapping(value = "/cards/attachments", method = RequestMethod.POST)
    public ResponseEntity<?> uploadAttachment(@RequestParam Long cardId, @RequestParam("file") MultipartFile file) {
        try {
            storageService.store(file, String.valueOf(cardId));
            Attachment attachment = attachmentService.addAttachmentToCard(cardId, file.getOriginalFilename());
            return ResponseEntity.ok(attachment);
        } catch (Exception e) {
            log.error("Attachment could not be added {}", e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @RequestMapping(value = "/cards/attachments/{attachmentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAttachment(@PathVariable Long attachmentId) {
        Attachment removedAttachment = attachmentService.removeAttachmentFromCard(attachmentId);
        storageService.delete(removedAttachment.getFileName(), String.valueOf(removedAttachment.getCard().getId()));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/cards/attachments/{attachmentId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) {
        Attachment attachment = attachmentService.findAttachmentById(attachmentId);
        Resource file = storageService.loadAsResource(attachment.getFileName(), String.valueOf(attachment.getCard().getId()));
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }

    @RequestMapping(value = "/cards/{cardId}/completion_date", method = RequestMethod.POST)
    public ResponseEntity<?> addCompletionDateToCard(@PathVariable Long cardId, @RequestBody CompletionDate completionDate) {
        cardService.changeCardCompletionDate(cardId, completionDate);
        return ResponseEntity.ok().build();
    }
}

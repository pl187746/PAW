package pl.iis.paw.trello.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.domain.RecordType;
import pl.iis.paw.trello.exception.CardNotFoundException;
import pl.iis.paw.trello.repository.CardRepository;

@Service
public class CardService {

	private final static Logger log = LoggerFactory.getLogger(CardService.class);

    private CardRepository cardRepository;
    private RecordService recordService;

    @Autowired
    public CardService(CardRepository cardRepository, RecordService recordService) {
        this.cardRepository = cardRepository;
        this.recordService = recordService;
    }
    
    public List<Card> getCards() {
    	return cardRepository.findAll();
    }
    
    public List<Card> getCards(Pageable pageable) {
    	return cardRepository.findAll(pageable).getContent();
    }
    
    public Card findCardById(Long cardId) {
    	return Optional
    			.ofNullable(cardRepository.findOne(cardId))
    			.orElseThrow(() -> new CardNotFoundException(cardId));
    }
    
    public Card addCard(Card card) {
    	card = cardRepository.save(card);
    	recordService.record(card.getCardList().getBoard(), RecordType.CARD_CREATE, card.getCardList().getName(), card.getName());
    	return card;
    }
    
    public Card updateCard(Card card) {
    	return updateCard(card.getId(), card);
    }
    
    public Card updateCard(Long id, Card card) {
    	Card existingCard = findCardById(id);
    	
    	Optional.ofNullable(card.getName())
		.filter(n -> !n.equals(existingCard.getName()))
		.ifPresent(n -> {
			recordService.record(existingCard.getCardList().getBoard(), RecordType.CARD_RENAME, existingCard.getName(), n);
			existingCard.setName(n);
		});

    	Optional.ofNullable(card.getOrd()).ifPresent(existingCard::setOrd);
    	
    	Optional.ofNullable(card.getListId())
    		.filter(i -> !i.equals(existingCard.getListId()))
    		.ifPresent(i -> {
    			recordService.record(existingCard.getCardList().getBoard(), RecordType.CARD_CHANGE_LIST, existingCard.getCardList().getName(), card.getCardList().getName());
    			existingCard.setListId(i);
    		});
    	
    	if(existingCard.isArchive() != card.isArchive()) {
    		recordService.record(existingCard.getCardList().getBoard(), (card.isArchive() ? RecordType.CARD_ARCHIVE : RecordType.CARD_UNARCHIVE), existingCard.getName());
    		existingCard.setArchive(card.isArchive());
    	}
    	
    	return cardRepository.save(existingCard);
    }
    
    public void deleteCard(Card card) {
    	recordService.record(card.getCardList().getBoard(), RecordType.CARD_DELETE, card.getName());
    	cardRepository.delete(card);
    }
    
    public void deleteCard(Long id) {
    	deleteCard(findCardById(id));
    }
	
}

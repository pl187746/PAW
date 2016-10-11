package pl.iis.paw.trello.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.exception.CardNotFoundException;
import pl.iis.paw.trello.repository.CardRepository;

@Service
public class CardService {

	private final static Logger log = LoggerFactory.getLogger(CardService.class);

    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
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
    	return cardRepository.save(card);
    }
    
    public Card updateCard(Card card) {
    	return updateCard(card.getId(), card);
    }
    
    public Card updateCard(Long id, Card card) {
    	Card existingCard = findCardById(id);
    	
    	existingCard.setName(card.getName());
    	
    	return cardRepository.save(card);
    }
    
    public void deleteCard(Card card) {
    	cardRepository.delete(card);
    }
    
    public void deleteCard(Long id) {
    	deleteCard(findCardById(id));
    }
	
}

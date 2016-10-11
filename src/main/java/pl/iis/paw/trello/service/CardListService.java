package pl.iis.paw.trello.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iis.paw.trello.domain.CardList;
import pl.iis.paw.trello.exception.CardListNotFoundException;
import pl.iis.paw.trello.repository.CardListRepository;

@Service
public class CardListService {

	private final static Logger log = LoggerFactory.getLogger(CardListService.class);

    private CardListRepository cardListRepository;

    @Autowired
    public CardListService(CardListRepository cardListRepository) {
        this.cardListRepository = cardListRepository;
    }
    
    public List<CardList> getCardLists() {
    	return cardListRepository.findAll();
    }
    
    public List<CardList> getCardLists(Pageable pageable) {
    	return cardListRepository.findAll(pageable).getContent();
    }
    
    public CardList findCardListById(Long cardListId) {
    	return Optional
    			.ofNullable(cardListRepository.findOne(cardListId))
    			.orElseThrow(() -> new CardListNotFoundException(cardListId));
    }
    
    public CardList addCardList(CardList cardList) {
    	return cardListRepository.save(cardList);
    }
    
    public CardList updateCardList(CardList cardList) {
    	return updateCardList(cardList.getId(), cardList);
    }
    
    public CardList updateCardList(Long id, CardList cardList) {
    	CardList existingCardList = findCardListById(id);
    	
    	existingCardList.setName(cardList.getName());
    	
    	return cardListRepository.save(cardList);
    }
    
    public void deleteCardList(CardList cardList) {
    	cardListRepository.delete(cardList);
    }
    
    public void deleteCardList(Long id) {
    	deleteCardList(findCardListById(id));
    }
	
}

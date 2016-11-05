package pl.iis.paw.trello.service;

import static pl.iis.paw.trello.service.RecordService.P.p;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.domain.CardList;
import pl.iis.paw.trello.domain.Label;
import pl.iis.paw.trello.domain.RecordType;
import pl.iis.paw.trello.exception.CardNotFoundException;
import pl.iis.paw.trello.repository.CardRepository;
import pl.iis.paw.trello.repository.LabelRepository;

@Service
public class CardService {

	private final static Logger log = LoggerFactory.getLogger(CardService.class);

    private CardRepository cardRepository;
    private RecordService recordService;
    private CardListService cardListService;
    private LabelRepository labelRepository;

    @Autowired
    public CardService(CardRepository cardRepository, RecordService recordService, CardListService cardListService, LabelRepository labelRepository) {
        this.cardRepository = cardRepository;
        this.recordService = recordService;
        this.cardListService = cardListService;
        this.labelRepository = labelRepository;
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
    	recordService.record(card.getCardList().getBoard(), RecordType.CARD_CREATE, p("listName", card.getCardList().getName()), p("cardName", card.getName()));
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
				recordService.record(existingCard.getCardList().getBoard(), RecordType.CARD_RENAME, p("oldCardName", existingCard.getName()), p("newCardName", n));
				existingCard.setName(n);
			});

    	Optional.ofNullable(card.getOrd()).ifPresent(existingCard::setOrd);
    	
    	Optional.ofNullable(card.getListId())
    		.filter(i -> !i.equals(existingCard.getListId()))
    		.ifPresent(i -> {
    			CardList dstCardList = cardListService.findCardListById(i);
    			recordService.record(existingCard.getCardList().getBoard(), RecordType.CARD_CHANGE_LIST, p("cardName", existingCard.getName()), p("oldListName", existingCard.getCardList().getName()), p("newListName", dstCardList.getName()));
    			existingCard.setCardList(dstCardList);
    		});
    	
    	if(existingCard.isArchive() != card.isArchive()) {
    		recordService.record(existingCard.getCardList().getBoard(), (card.isArchive() ? RecordType.CARD_ARCHIVE : RecordType.CARD_UNARCHIVE), p("cardName", existingCard.getName()));
    		existingCard.setArchive(card.isArchive());
    	}
    	
    	Optional.ofNullable(card.getLabels())
    		.map(lbs -> lbs.stream()
    			.map(lb -> labelRepository.findOne(lb.getId()))
    			.filter(li -> li != null)
    			.collect(Collectors.toList()))
    		.ifPresent(lbs -> {
    			Set<Long> oldLbIdSet = existingCard.getLabels().stream().map(Label::getId).collect(Collectors.toSet());    			
    			Set<Long> newLbIdSet = lbs.stream().map(Label::getId).collect(Collectors.toSet());
    			existingCard.getLabels().stream()
    				.filter(o -> !newLbIdSet.contains(o.getId()))
    				.forEach(o -> recordService.record(existingCard.getCardList().getBoard(), RecordType.CARD_REMOVE_LABEL, p("cardName", existingCard.getName()), p("labelName", o.getName()), p("labelColor", o.getColor())));
    			lbs.stream()
    				.filter(n -> !oldLbIdSet.contains(n.getId()))
    				.forEach(n -> recordService.record(existingCard.getCardList().getBoard(), RecordType.CARD_ADD_LABEL, p("cardName", existingCard.getName()), p("labelName", n.getName()), p("labelColor", n.getColor())));
    			existingCard.setLabels(lbs);
    		});
    	
    	return cardRepository.save(existingCard);
    }
    
    public void deleteCard(Card card) {
    	recordService.record(card.getCardList().getBoard(), RecordType.CARD_DELETE, p("cardName", card.getName()));
    	cardRepository.delete(card);
    }
    
    public void deleteCard(Long id) {
    	deleteCard(findCardById(id));
    }


	
}

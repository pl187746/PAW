package pl.iis.paw.trello.service;

import static pl.iis.paw.trello.service.RecordService.P.p;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.CardList;
import pl.iis.paw.trello.domain.RecordType;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.exception.CardListNotFoundException;
import pl.iis.paw.trello.repository.CardListRepository;

@Service
public class CardListService {

	private final static Logger log = LoggerFactory.getLogger(CardListService.class);

	private CardListRepository cardListRepository;
	private RecordService recordService;

	@Autowired
	public CardListService(CardListRepository cardListRepository, RecordService recordService) {
		this.cardListRepository = cardListRepository;
		this.recordService = recordService;
	}

	public List<CardList> getCardLists() {
		return cardListRepository.findAll();
	}

	public List<CardList> getCardLists(Pageable pageable) {
		return cardListRepository.findAll(pageable).getContent();
	}

	public CardList findCardListById(Long cardListId) {
		return Optional.ofNullable(cardListRepository.findOne(cardListId))
				.orElseThrow(() -> new CardListNotFoundException(cardListId));
	}

	public CardList addCardList(CardList cardList) {
		cardList = cardListRepository.save(cardList);
		List<User> subscribers = Optional.of(cardList.getBoard()).map(Board::getSubscribers).orElse(null);
		recordService.record(cardList.getBoard(), RecordType.LIST_CREATE, subscribers, p("listName", cardList.getName()));
		return cardList;
	}

	public CardList updateCardList(CardList cardList) {
		return updateCardList(cardList.getId(), cardList);
	}

	public CardList updateCardList(Long id, CardList cardList) {
		CardList existingCardList = findCardListById(id);
		List<User> subscribers = Optional.of(existingCardList.getBoard()).map(Board::getSubscribers).orElse(null);

		Optional.ofNullable(cardList.getName()).filter(n -> !n.equals(existingCardList.getName())).ifPresent(n -> {
			recordService.record(existingCardList.getBoard(), RecordType.LIST_RENAME, subscribers,
					p("oldListName", existingCardList.getName()), p("newListName", n));
			existingCardList.setName(n);
		});

		Optional.ofNullable(cardList.getBoard()).ifPresent(existingCardList::setBoard);
		Optional.ofNullable(cardList.getOrd()).ifPresent(existingCardList::setOrd);

		if (existingCardList.isArchive() != cardList.isArchive()) {
			recordService.record(existingCardList.getBoard(),
					(cardList.isArchive() ? RecordType.LIST_ARCHIVE : RecordType.LIST_UNARCHIVE), subscribers,
					p("listName", existingCardList.getName()));
			existingCardList.setArchive(cardList.isArchive());
		}

		return cardListRepository.save(existingCardList);
	}

	public void deleteCardList(CardList cardList) {
		List<User> subscribers = Optional.of(cardList.getBoard()).map(Board::getSubscribers).orElse(null);
		recordService.record(cardList.getBoard(), RecordType.LIST_DELETE, subscribers, p("listName", cardList.getName()));
		cardListRepository.delete(cardList);
	}

	public void deleteCardList(Long id) {
		deleteCardList(findCardListById(id));
	}

}

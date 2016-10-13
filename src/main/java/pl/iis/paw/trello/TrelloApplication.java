package pl.iis.paw.trello;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.domain.CardList;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.repository.BoardRepository;
import pl.iis.paw.trello.repository.CardListRepository;
import pl.iis.paw.trello.repository.CardRepository;
import pl.iis.paw.trello.repository.UserRepository;

@SpringBootApplication
public class TrelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrelloApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demoUsers(UserRepository userRepository) {
		return (args) -> {
			userRepository.save(new User("admin", "ap", "a@x.com"));
			userRepository.save(new User("bbb", "bp", "b@x.com"));
			userRepository.save(new User("ccc", "cp", "c@x.com"));
			userRepository.save(new User("ddd", "dp", "d@x.com"));
		};
	}
	
	@Bean
	public CommandLineRunner demoBoardsListsCards(BoardRepository boardRepository, CardListRepository cardListRepository, CardRepository cardRepository) {
		return (args) -> {
			Board board1 = boardRepository.save(new Board("Tabliczka 1", null));
			Board board2 = boardRepository.save(new Board("Tabliczka 2", null));
			boardRepository.save(new Board("Tabliczka 3", new ArrayList<>()));
			CardList list11 = cardListRepository.save(new CardList("Lista 1/1", board1, null));
			cardListRepository.save(new CardList("Lista 1/2", board1, null));
			CardList list13 = cardListRepository.save(new CardList("Lista 1/3", board1, null));
			CardList list21 = cardListRepository.save(new CardList("Lista 2/1", board2, null));
			cardRepository.save(new Card("Karta 1/1/1", list11));
			cardRepository.save(new Card("Karta 1/1/2", list11));
			cardRepository.save(new Card("Karta 1/3/1", list13));
			cardRepository.save(new Card("Karta 1/3/2", list13));
			cardRepository.save(new Card("Karta 1/3/3", list13));
			cardRepository.save(new Card("Karta 1/3/4", list13));
			cardRepository.save(new Card("Karta 2/1/1", list21));
		};
	}
	
}

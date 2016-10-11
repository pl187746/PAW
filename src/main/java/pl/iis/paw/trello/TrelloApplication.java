package pl.iis.paw.trello;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pl.iis.paw.trello.domain.Board;
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
			userRepository.save(new User("aaa", "ap", "a@x.com"));
			userRepository.save(new User("bbb", "bp", "b@x.com"));
			userRepository.save(new User("ccc", "cp", "c@x.com"));
			userRepository.save(new User("ddd", "dp", "d@x.com"));
		};
	}
	
	@Bean
	public CommandLineRunner demoBoardsLists(BoardRepository boardRepository, CardListRepository cardListRepository) {
		return (args) -> {
			Board board1 = boardRepository.save(new Board("Tabliczka 1", new ArrayList<>()));
			board1.getLists().add(new CardList("Lista 1/1", board1, new ArrayList<>()));
			board1.getLists().add(new CardList("Lista 1/2", board1, new ArrayList<>()));
			board1.getLists().add(new CardList("Lista 1/3", board1, new ArrayList<>()));
			cardListRepository.save(board1.getLists());
			board1 = boardRepository.save(board1);
			Board board2 = boardRepository.save(new Board("Tabliczka 2", new ArrayList<>()));
			board2.getLists().add(new CardList("Lista 2/1", board2, new ArrayList<>()));
			cardListRepository.save(board2.getLists());
			board2 = boardRepository.save(board2);
			boardRepository.save(new Board("Tabliczka 3", new ArrayList<>()));
		};
	}
	
}

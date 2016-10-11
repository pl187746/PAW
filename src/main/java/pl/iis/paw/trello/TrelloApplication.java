package pl.iis.paw.trello;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pl.iis.paw.trello.domain.Board;
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
	public CommandLineRunner demoBoards(BoardRepository boardRepository) {
		return (args) -> {
			boardRepository.save(new Board("Tabliczka 1", null));
			boardRepository.save(new Board("Tabliczka 2", null));
			boardRepository.save(new Board("Tabliczka 3", null));
		};
	}
	
}

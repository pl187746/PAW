package pl.iis.paw.trello;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.domain.CardList;
import pl.iis.paw.trello.domain.FavBoard;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.repository.BoardRepository;
import pl.iis.paw.trello.repository.CardListRepository;
import pl.iis.paw.trello.repository.CardRepository;
import pl.iis.paw.trello.repository.FavBoardRepository;
import pl.iis.paw.trello.repository.UserRepository;

@SpringBootApplication
public class TrelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrelloApplication.class, args);
	}
}

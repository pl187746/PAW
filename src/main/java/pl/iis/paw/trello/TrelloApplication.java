package pl.iis.paw.trello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.iis.paw.trello.domain.User;
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
	
}

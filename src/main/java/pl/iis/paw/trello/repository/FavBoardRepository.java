package pl.iis.paw.trello.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.FavBoard;
import pl.iis.paw.trello.domain.User;

@Repository
public interface FavBoardRepository extends JpaRepository<FavBoard, Long> {
	
	List<FavBoard> findByUser(User user);
	
	List<FavBoard> findByBoard(Board board);
	
	FavBoard findByUserAndBoard(User user, Board board);

}

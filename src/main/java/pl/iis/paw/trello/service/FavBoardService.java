package pl.iis.paw.trello.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.FavBoard;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.exception.FavBoardNotFoundException;
import pl.iis.paw.trello.repository.FavBoardRepository;

@Service
public class FavBoardService {
	
	private final static Logger log = LoggerFactory.getLogger(BoardService.class);
	
	private FavBoardRepository favBoardRepository;

	@Autowired
	public FavBoardService(FavBoardRepository favBoardRepository) {
		super();
		this.favBoardRepository = favBoardRepository;
	}
	
	public List<FavBoard> getFavBoards() {
		return favBoardRepository.findAll();
	}
	
	public List<FavBoard> getFavBoards(Pageable pageable) {
		return favBoardRepository.findAll(pageable).getContent();
	}
	
	public FavBoard getFavBoardById(Long favBoardId) {
		return Optional
				.ofNullable(favBoardRepository.findOne(favBoardId))
				.orElseThrow(() -> new FavBoardNotFoundException(favBoardId));
	}
	
	public List<FavBoard> getFavBoardsByUser(User user) {
		return favBoardRepository.findByUser(user);
	}
	
	public List<FavBoard> getFavBoardsByBoard(Board board) {
		return favBoardRepository.findByBoard(board);
	}
	
	public Optional<FavBoard> getFavBoardByUserAndBoard(User user, Board board) {
		return Optional.ofNullable(favBoardRepository.findByUserAndBoard(user, board));
	}
	
	public FavBoard addFavBoard(FavBoard favBoard) {
		return favBoardRepository.save(favBoard);
	}
	
	public void deleteFavBoard(FavBoard favBoard) {
		favBoardRepository.delete(favBoard);
	}
	
	public void deleteFavBoard(Long favBoardId) {
		deleteFavBoard(getFavBoardById(favBoardId));
	}

}

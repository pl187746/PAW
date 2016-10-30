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
import pl.iis.paw.trello.domain.RecordType;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.exception.FavBoardNotFoundException;
import pl.iis.paw.trello.repository.FavBoardRepository;

@Service
public class FavBoardService {
	
	private final static Logger log = LoggerFactory.getLogger(BoardService.class);
	
	private FavBoardRepository favBoardRepository;
	private RecordService recordService;

	@Autowired
	public FavBoardService(FavBoardRepository favBoardRepository, RecordService recordService) {
		super();
		this.favBoardRepository = favBoardRepository;
		this.recordService = recordService;
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
		favBoard = favBoardRepository.save(favBoard);
		recordService.record(favBoard.getBoard(), RecordType.BOARD_LIKE);
		return favBoard;
	}
	
	public void deleteFavBoard(FavBoard favBoard) {
		recordService.record(favBoard.getBoard(), RecordType.BOARD_UNLIKE);
		favBoardRepository.delete(favBoard);
	}
	
	public void deleteFavBoard(Long favBoardId) {
		deleteFavBoard(getFavBoardById(favBoardId));
	}

}

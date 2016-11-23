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
import pl.iis.paw.trello.domain.RecordType;
import pl.iis.paw.trello.exception.BoardNotFoundException;
import pl.iis.paw.trello.repository.BoardRepository;

@Service
public class BoardService {

	private final static Logger log = LoggerFactory.getLogger(BoardService.class);

    private BoardRepository boardRepository;
    private RecordService recordService;

    @Autowired
    public BoardService(BoardRepository boardRepository, RecordService recordService) {
        this.boardRepository = boardRepository;
        this.recordService = recordService;
    }
    
    public List<Board> getBoards() {
    	return boardRepository.findAll();
    }

    public List<Board> getBoards(Pageable pageable) {
    	return boardRepository.findAll(pageable).getContent();
    }
    
    public Board findBoardById(Long boardId) {
    	return Optional
    			.ofNullable(boardRepository.findOne(boardId))
    			.orElseThrow(() -> new BoardNotFoundException(boardId));
    }

	public Board findBoardByShareUrl(String shareLink) {
		long id = Long.parseLong(shareLink,16) - 1234567890;
		return findBoardById(id);
	}
    
    public Board addBoard(Board board) {
    	board = boardRepository.save(board);
    	recordService.record(board, RecordType.BOARD_CREATE, p("boardName", board.getName()));
    	return board;
    }
    
    public Board updateBoard(Board board) {
    	return updateBoard(board.getId(), board);
    }
    
    public Board updateBoard(Long id, Board board) {
    	Board existingBoard = findBoardById(id);
    	
    	Optional.ofNullable(board.getName())
    		.filter(n -> !n.equals(existingBoard.getName()))
    		.ifPresent(n -> {
    			recordService.record(existingBoard, RecordType.BOARD_RENAME, p("oldBoardName", existingBoard.getName()), p("newBoardName", n));
    			existingBoard.setName(n);
    		});
    	
    	Optional.ofNullable(board.getMembers())
    		.ifPresent(existingBoard::setMembers);
    	
    	Optional.ofNullable(board.getTeam())
    		.ifPresent(team -> existingBoard.setTeam((team.getId() != null) ? team : null));
    	
    	return boardRepository.save(existingBoard);
    }
    
    public void deleteBoard(Board board) {
    	boardRepository.delete(board);
    }
    
    public void deleteBoard(Long id) {
    	deleteBoard(findBoardById(id));
    }
}

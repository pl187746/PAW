package pl.iis.paw.trello.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.BoardInfo;
import pl.iis.paw.trello.exception.BoardNotFoundException;
import pl.iis.paw.trello.repository.BoardRepository;

@Service
public class BoardService {

	private final static Logger log = LoggerFactory.getLogger(BoardService.class);

    private BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    
    public List<Board> getBoards() {
    	return boardRepository.findAll();
    }
    
    public List<Board> getBoards(Pageable pageable) {
    	return boardRepository.findAll(pageable).getContent();
    }
    
    public List<BoardInfo> getBoardInfos() {
    	return boardsToBoardInfos(getBoards());
    }
    
    public List<BoardInfo> getBoardInfos(Pageable pageable) {
    	return boardsToBoardInfos(getBoards(pageable));
    }
    
    public static List<BoardInfo> boardsToBoardInfos(Collection<Board> boards) {
    	return boards.stream().map(Board::getInfo).collect(Collectors.toList());
    }
    
    public Board findBoardById(Long boardId) {
    	return Optional
    			.ofNullable(boardRepository.findOne(boardId))
    			.orElseThrow(() -> new BoardNotFoundException(boardId));
    }
    
    public Board addBoard(Board board) {
    	return boardRepository.save(board);
    }
    
    public Board updateBoard(Board board) {
    	return updateBoard(board.getId(), board);
    }
    
    public Board updateBoard(Long id, Board board) {
    	Board existingBoard = findBoardById(id);
    	
    	existingBoard.setName(board.getName());
    	
    	return boardRepository.save(existingBoard);
    }
    
    public void deleteBoard(Board board) {
    	boardRepository.delete(board);
    }
    
    public void deleteBoard(Long id) {
    	deleteBoard(findBoardById(id));
    }
	
}

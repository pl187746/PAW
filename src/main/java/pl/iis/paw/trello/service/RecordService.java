package pl.iis.paw.trello.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.Record;
import pl.iis.paw.trello.domain.RecordType;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.repository.RecordRepository;

@Service
public class RecordService {
	
	private final static Logger log = LoggerFactory.getLogger(RecordService.class);
	
	private RecordRepository recordRepository;

	@Autowired
	public RecordService(RecordRepository recordRepository) {
		this.recordRepository = recordRepository;
	}
	
	public List<Record> getRecords() {
		return recordRepository.findAll();
	}
	
	public List<Record> getRecords(Pageable pageable) {
		return recordRepository.findAll(pageable).getContent();
	}
	
	public User currentUser() {
		User user = new User();
		user.setId(1L);
		return user;
	}
	
	public void record(Board board, RecordType type, String... params) {
		Record rec = new Record(type, new Date(), board, currentUser(), Arrays.asList(params));
		recordRepository.save(rec);
	}
	
	public void record(Long boardId, RecordType type, String... params) {
		Board board = new Board();
		board.setId(boardId);
		record(board, type, params);
	}

}

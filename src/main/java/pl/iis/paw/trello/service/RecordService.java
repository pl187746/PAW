package pl.iis.paw.trello.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private UserService userService;

	@Autowired
	public RecordService(RecordRepository recordRepository, UserService userService) {
		this.recordRepository = recordRepository;
		this.userService = userService;
	}

	public List<Record> getRecords() {
		return recordRepository.findAll();
	}

	public List<Record> getRecords(Pageable pageable) {
		return recordRepository.findAll(pageable).getContent();
	}

	public List<Record> getRecordsOfBoardId(Long boardId) {
		Board board = new Board();
		board.setId(boardId);
		return getRecordsOfBoard(board);
	}

	public List<Record> getRecordsOfBoard(Board board) {
		return recordRepository.findByBoard(board);
	}

	public List<Record> getRecordsOfBoardIdAfterDate(Long boardId, Date date) {
		Board board = new Board();
		board.setId(boardId);
		return getRecordsOfBoardAfterDate(board, date);
	}

	public List<Record> getRecordsOfBoardAfterDate(Board board, Date date) {
		return recordRepository.findByBoardAndTimestampAfter(board, date);
	}

	public User currentUser() {
		return userService.getCurrentUser();
	}

	public void record(Board board, RecordType type, List<User> notifiedUsers, Map<String, String> params) {
		notifiedUsers = new ArrayList<>(notifiedUsers);
		Record rec = new Record(type, new Date(), board, currentUser(), notifiedUsers, params);
		recordRepository.save(rec);
	}

	public void record(Board board, RecordType type, List<User> notifiedUsers, P... params) {
		record(board, type, notifiedUsers, P.toMap(params));
	}

	public void record(Long boardId, RecordType type, List<User> notifiedUsers, P... params) {
		Board board = new Board();
		board.setId(boardId);
		record(board, type, notifiedUsers, params);
	}

	public static class P {
		public String name;
		public String value;

		public P(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public static P p(String name, String value) {
			return new P(name, value);
		}

		public static Map<String, String> toMap(P[] array) {
			Map<String, String> map = new HashMap<>();
			Arrays.stream(array).forEach(p -> map.put(p.name, p.value));
			return map;
		}
	}

}

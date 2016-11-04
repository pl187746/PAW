package pl.iis.paw.trello.web.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.iis.paw.trello.service.RecordService;

@RestController
public class RecordController {
	
	private RecordService recordService;
	
	@Autowired
	public RecordController(RecordService recordService) {
		this.recordService = recordService;
	}
	
	@RequestMapping(value = "/records", params = { "boardId" }, method = RequestMethod.GET)
	public ResponseEntity<?> getRecordsOfBoard(@RequestParam("boardId") Long boardId) {
		return ResponseEntity.ok(recordService.getRecordsOfBoardId(boardId));
	}
	
	@RequestMapping(value = "/records", params = { "boardId", "dateAfter" }, method = RequestMethod.GET)
	public ResponseEntity<?> getRecordsOfBoard(@RequestParam("boardId") Long boardId, @RequestParam("dateAfter") Date date) {
		return ResponseEntity.ok(recordService.getRecordsOfBoardIdAfterDate(boardId, date));
	}

}

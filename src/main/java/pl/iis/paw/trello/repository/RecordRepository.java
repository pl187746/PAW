package pl.iis.paw.trello.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.stereotype.Repository;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.Record;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
	
	public List<Record> findByBoard(Board board);

	public List<Record> findByBoardAndTimestampAfter(Board board, @Temporal(TemporalType.TIMESTAMP) Date timestamp);
	
}

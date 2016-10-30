package pl.iis.paw.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.iis.paw.trello.domain.Record;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

}

package pl.iis.paw.trello.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
	
	public List<Label> findByBoard(Board board);

}

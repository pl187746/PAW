package pl.iis.paw.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iis.paw.trello.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}

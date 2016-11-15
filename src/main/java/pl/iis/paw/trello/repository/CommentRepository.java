package pl.iis.paw.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iis.paw.trello.domain.Comment;

/**
 * Created by Krystian on 2016-11-05.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}

package pl.iis.paw.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iis.paw.trello.domain.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

}

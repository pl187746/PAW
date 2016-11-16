package pl.iis.paw.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.iis.paw.trello.domain.Attachment;
import pl.iis.paw.trello.domain.Card;
import pl.iis.paw.trello.exception.AttachmentNotFoundException;
import pl.iis.paw.trello.repository.AttachmentRepository;

import java.util.Optional;

@Service
public class AttachmentService {

    private AttachmentRepository attachmentRepository;
    private CardService cardService;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository, CardService cardService) {
        this.attachmentRepository = attachmentRepository;
        this.cardService = cardService;
    }

    public Attachment findAttachmentById(Long attachmentId) {
        return Optional
            .ofNullable(attachmentRepository.findOne(attachmentId))
            .orElseThrow(() -> new AttachmentNotFoundException(attachmentId));
    }

    public Attachment addAttachmentToCard(Long cardId, String fileName) {
        Card card = cardService.findCardById(cardId);
        Attachment attachment = new Attachment(card, fileName);
        attachmentRepository.save(attachment);
        return attachment;
    }

    public Attachment removeAttachmentFromCard(Long attachmentId) {
        Attachment attachment = findAttachmentById(attachmentId);
        attachmentRepository.delete(attachment);
        return attachment;
    }
}

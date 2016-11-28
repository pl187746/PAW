package pl.iis.paw.trello.service;

import static pl.iis.paw.trello.service.RecordService.P.p;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.iis.paw.trello.domain.Board;
import pl.iis.paw.trello.domain.Label;
import pl.iis.paw.trello.domain.RecordType;
import pl.iis.paw.trello.exception.LabelNotFoundException;
import pl.iis.paw.trello.repository.LabelRepository;

@Service
public class LabelService {

	private final static Logger log = LoggerFactory.getLogger(LabelService.class);

	private LabelRepository labelRepository;
	private RecordService recordService;
	private CardService cardService;

	@Autowired
	public LabelService(LabelRepository labelRepository, RecordService recordService, CardService cardService) {
		super();
		this.labelRepository = labelRepository;
		this.recordService = recordService;
		this.cardService = cardService;
	}

	public List<Label> getLabels() {
		return labelRepository.findAll();
	}

	public List<Label> getLabels(Pageable pageable) {
		return labelRepository.findAll(pageable).getContent();
	}

	public Label getLabelById(Long labelId) {
		return Optional.ofNullable(labelRepository.findOne(labelId))
				.orElseThrow(() -> new LabelNotFoundException(labelId));
	}

	public List<Label> getLabelsByBoardId(Long boardId) {
		Board board = new Board();
		board.setId(boardId);
		return getLabelsByBoard(board);
	}

	public List<Label> getLabelsByBoard(Board board) {
		return labelRepository.findByBoard(board);
	}

	public Label addLabel(Label label) {
		label = labelRepository.save(label);
		recordService.record(label.getBoard(), RecordType.LABEL_CREATE, null, p("labelName", label.getName()),
				p("labelColor", label.getColor()));
		return label;
	}

	public Label updateLabel(Label label) {
		return updateLabel(label.getId(), label);
	}

	public Label updateLabel(Long labelId, Label label) {
		Label existingLabel = labelRepository.findOne(labelId);

		Optional.ofNullable(label.getBoard()).ifPresent(existingLabel::setBoard);

		Optional.ofNullable(label.getName()).filter(n -> !n.equals(existingLabel.getName())).ifPresent(n -> {
			recordService.record(existingLabel.getBoard(), RecordType.LABEL_RENAME, null,
					p("oldLabelName", existingLabel.getName()), p("newLabelName", n),
					p("labelColor", existingLabel.getColor()));
			existingLabel.setName(n);
		});

		Optional.ofNullable(label.getColor()).filter(c -> !c.equals(existingLabel.getColor())).ifPresent(c -> {
			recordService.record(existingLabel.getBoard(), RecordType.LABEL_CHANGE_COLOR, null,
					p("labelName", existingLabel.getName()), p("oldLabelColor", existingLabel.getColor()),
					p("newLabelColor", c));
			existingLabel.setColor(c);
		});

		return labelRepository.save(existingLabel);
	}

	public void deleteLabel(Label label) {
		label.getLabeledCards().forEach(card -> {
			if (card.getLabels().removeIf(lb -> label.getId().equals(lb.getId()))) {
				cardService.updateCard(card);
			}
		});
		recordService.record(label.getBoard(), RecordType.LABEL_DELETE, null, p("labelName", label.getName()),
				p("labelColor", label.getColor()));
		labelRepository.delete(label);
	}

	public void deleteLabel(Long labelId) {
		deleteLabel(labelRepository.findOne(labelId));
	}

}

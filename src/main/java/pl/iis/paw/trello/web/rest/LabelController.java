package pl.iis.paw.trello.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.iis.paw.trello.domain.Label;
import pl.iis.paw.trello.service.LabelService;

@RestController
public class LabelController {
	
	private LabelService labelService;

	@Autowired
	public LabelController(LabelService labelService) {
		super();
		this.labelService = labelService;
	}

	@RequestMapping(value = "/labels", method = RequestMethod.GET)
    public ResponseEntity<?> getLabels(Pageable pageable) {
        return ResponseEntity.ok(labelService.getLabels(pageable));
    }
	
	@RequestMapping(value = "/labels", params = { "boardId" }, method = RequestMethod.GET)
    public ResponseEntity<?> getLabelsByBoard(@RequestParam("boardId") Long boardId) {
        return ResponseEntity.ok(labelService.getLabelsByBoardId(boardId));
    }
	
	@RequestMapping(value = "/labels/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getLabel(@PathVariable(value = "id") Long labelId) {
        return ResponseEntity.ok(labelService.getLabelById(labelId));
    }
	
	@RequestMapping(value = "/labels", method = RequestMethod.POST)
    public ResponseEntity<?> createLabel(@Valid @RequestBody Label label) throws URISyntaxException {
        return ResponseEntity
            .created(new URI("/labels/" + label.getId()))
            .body(labelService.addLabel(label));
    }

	@RequestMapping(value = "/labels", method = RequestMethod.PUT)
    public ResponseEntity<?> updateLabel(@RequestBody Label label) {
        return ResponseEntity.ok(labelService.updateLabel(label));
    }

    @RequestMapping(value = "/labels/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.ok().build();
    }

}

package pl.iis.paw.trello.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.iis.paw.trello.domain.Team;
import pl.iis.paw.trello.service.TeamService;

@RestController
public class TeamController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TeamService teamService;
	
	@RequestMapping(value = "/teams", method = RequestMethod.GET)
    public ResponseEntity<?> getTeams(Pageable pageable) {
        return ResponseEntity.ok(teamService.getTeams(pageable));
    }
	
	@RequestMapping(value = "/teams/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTeam(@PathVariable(value = "id") Long teamId) {
        return ResponseEntity.ok(teamService.findTeamById(teamId));
    }

    @RequestMapping(value = "/teams", method = RequestMethod.POST)
    public ResponseEntity<?> createTeam(@Valid @RequestBody Team team) throws URISyntaxException {
        log.info("Creating team with name {}", team.getName());
        
        return ResponseEntity
            .created(new URI("/teams/" + team.getId()))
            .body(teamService.addTeam(team));
    }

    @RequestMapping(value = "/teams", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTeam(@RequestBody Team team) {
        log.info("Updating team with id {}", team.getId());
        return ResponseEntity.ok(teamService.updateTeam(team));
    }

    @RequestMapping(value = "/teams/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        log.info("Removing team with id {}", id);

        teamService.deleteTeam(id);
        return ResponseEntity.ok().build();
    }

}

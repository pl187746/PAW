package pl.iis.paw.trello.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.iis.paw.trello.domain.Team;
import pl.iis.paw.trello.exception.TeamNotFoundException;
import pl.iis.paw.trello.repository.TeamRepository;

@Service
public class TeamService {

	private final static Logger log = LoggerFactory.getLogger(TeamService.class);

	private TeamRepository teamRepository;

	@Autowired
	public TeamService(TeamRepository teamRepository) {
		super();
		this.teamRepository = teamRepository;
	}

	public List<Team> getTeams() {
		return teamRepository.findAll();
	}

	public List<Team> getTeams(Pageable pageable) {
		return teamRepository.findAll(pageable).getContent();
	}

	public Team findTeamById(Long teamId) {
		return Optional
				.ofNullable(teamRepository.findOne(teamId))
				.orElseThrow(() -> new TeamNotFoundException(teamId));
	}

	public Team addTeam(Team team) {
		team = teamRepository.save(team);
		return team;
	}

	public Team updateTeam(Team team) {
		return updateTeam(team.getId(), team);
	}

	private Team updateTeam(Long teamId, Team team) {
		Team existingTeam = findTeamById(teamId);
		
		Optional.ofNullable(team.getName()).ifPresent(existingTeam::setName);
		Optional.ofNullable(team.getUsers()).ifPresent(existingTeam::setUsers);
		Optional.ofNullable(team.getBoards()).ifPresent(existingTeam::setBoards);
		
		return teamRepository.save(existingTeam);
	}

	public void deleteTeam(Team team) {
		teamRepository.delete(team);
	}

	public void deleteTeam(Long teamId) {
		deleteTeam(findTeamById(teamId));
	}

}

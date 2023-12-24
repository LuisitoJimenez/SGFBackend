package com.coatl.sac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.ClubTeamEntity;
import com.coatl.sac.entity.PlayerEntity;
import com.coatl.sac.entity.TeamEntity;
import com.coatl.sac.entity.TeamPlayerEntity;
import com.coatl.sac.entity.TeamSubEntity;
import com.coatl.sac.json.UserName;
import com.coatl.sac.model.TeamAssignmentDTO;
import com.coatl.sac.model.TeamDTO;
import com.coatl.sac.repository.ClubRepository;
import com.coatl.sac.repository.ClubTeamRepository;
import com.coatl.sac.repository.PlayerRepository;
import com.coatl.sac.repository.TeamPlayerRepository;
import com.coatl.sac.repository.TeamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    private final ClubRepository clubRepository;

    private final ClubTeamRepository clubTeamRespository;

    private final PlayerRepository playerRepository;

    private final TeamPlayerRepository teamPlayerRepository;

    private final TeamSubRepository teamSubRepository;

    public List<Map<String, Object>> getTeamList() {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> teamList = teamRepository.getTeamList();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> team : teamList) {
            Map<String, Object> newTeam = new HashMap<>(team);
            String coachString = (String) newTeam.get("coach");
            try {
                Map<String, Object> coach = mapper.readValue(coachString, new TypeReference<Map<String, Object>>() {
                });
                newTeam.put("coach", coach);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            result.add(newTeam);
        }
        return result;

    }

    @Transactional
    public WebServiceResponse createTeam(TeamDTO teamDTO, Integer clubId) {
        try {

            TeamEntity team = new TeamEntity();
            team.setName(teamDTO.getName());
            team.setCoach(new UserName(teamDTO.getCoach().getFirst(), teamDTO.getCoach().getLast(),
                    teamDTO.getCoach().getSecondLast()));
            team.setUserCreated(1);
            teamRepository.save(team);

            Optional<ClubTeamEntity> clubTeam = clubTeamRespository.findByClubIdAndTeamId(clubId, team.getId());

            if (clubTeam.isPresent()) {
                throw new RuntimeException("Team already assigned to club");
            }

            saveClubTeam(team.getId(), clubId);

            saveTeamSub(team.getId(), teamDTO.getSubId());

            return new WebServiceResponse(true, "Team created successfully");
        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
        }
    }

    private void saveClubTeam(Integer teamId, Integer clubId) {
        ClubTeamEntity clubTeamEntity = new ClubTeamEntity();
        clubTeamEntity.setClubId(clubId);
        clubTeamEntity.setTeamId(teamId);
        clubTeamEntity.setUserCreated(1);
        clubTeamRespository.save(clubTeamEntity);
    }

    private void saveTeamSub(Integer teamId, Integer subId) {
        TeamSubEntity teamSubEntity = new TeamSubEntity();
        teamSubEntity.setTeamId(teamId);
        teamSubEntity.setSubId(subId);
        teamSubEntity.setUserCreated(1);
        teamSubRepository.save(teamSubEntity);
    }

    @Transactional
    public WebServiceResponse addTitularPlayerToTeam(TeamAssignmentDTO teamAssignmentDTO) {
        try {
            TeamEntity team = teamRepository.findById(teamAssignmentDTO.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));

            for (Integer playerId : teamAssignmentDTO.getPlayersIds()) {
                PlayerEntity player = playerRepository.findById(playerId)
                        .orElseThrow(() -> new RuntimeException("Player not found"));

                Optional<TeamPlayerEntity> teamPlayerEntity = teamPlayerRepository
                        .findByTeamIdAndPlayerId(teamAssignmentDTO.getTeamId(), playerId);

                if (teamPlayerEntity.isPresent()) {
                    throw new RuntimeException("Player already assigned to team");
                }

                TeamPlayerEntity teamPlayer = new TeamPlayerEntity();
                teamPlayer.setTeamId(teamAssignmentDTO.getTeamId());
                teamPlayer.setPlayerId(playerId);
                // teamPlayer.setPositionId(1);
                teamPlayer.setTypePlayerId(1);
                teamPlayer.setUserCreated(1);
                teamPlayerRepository.save(teamPlayer);
            }

            return new WebServiceResponse(true, "Player assigned successfully");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new WebServiceResponse(false, e.getMessage());
        }
    }

    @Transactional
    public WebServiceResponse addSubstitutePlayerToTeam(TeamAssignmentDTO teamAssignmentDTO) {
        try {

            TeamEntity team = teamRepository.findById(teamAssignmentDTO.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));

            for (Integer playerId : teamAssignmentDTO.getPlayersIds()) {
                PlayerEntity player = playerRepository.findById(playerId)
                        .orElseThrow(() -> new RuntimeException("Player not found"));

                Optional<TeamPlayerEntity> teamPlayerEntity = teamPlayerRepository
                        .findByTeamIdAndPlayerId(teamAssignmentDTO.getTeamId(), playerId);

                if (teamPlayerEntity.isPresent()) {
                    throw new RuntimeException("Player already assigned to team");
                }

                TeamPlayerEntity teamPlayer = new TeamPlayerEntity();
                teamPlayer.setTeamId(teamAssignmentDTO.getTeamId());
                teamPlayer.setPlayerId(playerId);
                // teamPlayer.setPositionId(1);
                teamPlayer.setUserCreated(1);
                teamPlayer.setTypePlayerId(2);
                teamPlayerRepository.save(teamPlayer);
            }
            return new WebServiceResponse(true, "Player assigned successfully");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new WebServiceResponse(false, e.getMessage());
        }
    }

    public List<Map<String, Object>> getTitularsPlayers(Integer teamId) {
        return teamPlayerRepository.getTitularPlayersByTeamId(teamId);
    }

    public List<Map<String, Object>> getSubstitutesPlayers(Integer teamId) {
        return teamPlayerRepository.getSubstitutePlayersByTeamId(teamId);
    }

    public Map<String, Object> getTeamById(Integer teamId) {
        return teamRepository.getTeamById(teamId);
    }
}

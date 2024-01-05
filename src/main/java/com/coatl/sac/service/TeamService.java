package com.coatl.sac.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.coatl.sac.dto.WebServiceResponse;
import com.coatl.sac.entity.ClubTeamEntity;
import com.coatl.sac.entity.GenderEntity;
import com.coatl.sac.entity.SubEntity;
import com.coatl.sac.entity.TeamEntity;
import com.coatl.sac.entity.TeamGenderEntity;
import com.coatl.sac.entity.TeamPlayerEntity;
import com.coatl.sac.entity.TeamSubEntity;
import com.coatl.sac.model.TeamAssignmentDTO;
import com.coatl.sac.model.TeamDTO;
import com.coatl.sac.repository.ClubTeamRepository;
import com.coatl.sac.repository.GenderRepository;
import com.coatl.sac.repository.PlayerRepository;
import com.coatl.sac.repository.SubRepository;
import com.coatl.sac.repository.TeamGenderRepository;
import com.coatl.sac.repository.TeamPlayerRepository;
import com.coatl.sac.repository.TeamRepository;
import com.coatl.sac.repository.TeamSubRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final ClubTeamRepository clubTeamRespository;

    private final PlayerRepository playerRepository;

    private final TeamPlayerRepository teamPlayerRepository;

    private final TeamSubRepository teamSubRepository;

    private final TeamGenderRepository teamGenderRepository;

    private final GenderRepository genderRepository;

    private final SubRepository subsRepository;

    public List<Map<String, Object>> getTeamList() {
        return teamRepository.getTeamList();
    }

    @Transactional
    public WebServiceResponse createTeam(TeamDTO teamDTO, Integer clubId) {
        try {

            TeamEntity team = new TeamEntity();
            team.setName(teamDTO.getTeamName());
            team.setCoach(teamDTO.getCoachName());
            team.setCreatedBy(1);
            teamRepository.save(team);

            Optional<ClubTeamEntity> clubTeam = clubTeamRespository.findByClubIdAndTeamId(clubId, team.getId());

            if (clubTeam.isPresent()) {
                throw new RuntimeException("Team already assigned to club");
            }

            Optional<GenderEntity> gender = genderRepository.findById(teamDTO.getGenderId());            
            Optional<SubEntity> sub = subsRepository.findById(teamDTO.getSubId());

            if (!gender.isPresent() || !sub.isPresent()) {
                throw new RuntimeException("Gender or Sub not found");
            }

            saveClubTeam(team.getId(), clubId);

            saveTeamSub(team.getId(), teamDTO.getSubId());

            saveTeamGender(team.getId(), teamDTO.getGenderId());

            return new WebServiceResponse(true, "Team created successfully");
        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
        }
    }

    private void saveClubTeam(Integer teamId, Integer clubId) {
        ClubTeamEntity clubTeamEntity = new ClubTeamEntity();
        clubTeamEntity.setClubId(clubId);
        clubTeamEntity.setTeamId(teamId);
        clubTeamEntity.setCreatedBy(1);
        clubTeamRespository.save(clubTeamEntity);
    }

    private void saveTeamSub(Integer teamId, Integer subId) {
        TeamSubEntity teamSubEntity = new TeamSubEntity();
        teamSubEntity.setTeamId(teamId);
        teamSubEntity.setSubId(subId);
        teamSubEntity.setCreatedBy(1);
        teamSubRepository.save(teamSubEntity);
    }

    private void saveTeamGender(Integer teamId, Integer genderId) {
        TeamGenderEntity teamGender = new TeamGenderEntity();
        teamGender.setTeamId(teamId);
        teamGender.setGenderId(genderId);
        teamGender.setCreatedBy(1);
        teamGenderRepository.save(teamGender);
    }

    @Transactional
    public WebServiceResponse addTitularPlayerToTeam(TeamAssignmentDTO teamAssignmentDTO) {
        try {

            teamRepository.findById(teamAssignmentDTO.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));

            for (Integer playerId : teamAssignmentDTO.getPlayersIds()) {

                playerRepository.findById(playerId)
                        .orElseThrow(() -> new RuntimeException("Player not found"));

                Optional<TeamPlayerEntity> teamPlayerEntity = teamPlayerRepository
                        .findByTeamIdAndPlayerId(teamAssignmentDTO.getTeamId(), playerId);

                if (teamPlayerEntity.isPresent()) {
                    throw new RuntimeException("Player already assigned to team");
                }

                TeamPlayerEntity teamPlayer = new TeamPlayerEntity();
                teamPlayer.setTeamId(teamAssignmentDTO.getTeamId());
                teamPlayer.setPlayerId(playerId);
                teamPlayer.setTypePlayerId(1);
                teamPlayer.setCreatedBy(1);
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

            teamRepository.findById(teamAssignmentDTO.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));

            for (Integer playerId : teamAssignmentDTO.getPlayersIds()) {

                playerRepository.findById(playerId)
                        .orElseThrow(() -> new RuntimeException("Player not found"));

                Optional<TeamPlayerEntity> teamPlayerEntity = teamPlayerRepository
                        .findByTeamIdAndPlayerId(teamAssignmentDTO.getTeamId(), playerId);

                if (teamPlayerEntity.isPresent()) {
                    throw new RuntimeException("Player already assigned to team");
                }

                TeamPlayerEntity teamPlayer = new TeamPlayerEntity();
                teamPlayer.setTeamId(teamAssignmentDTO.getTeamId());
                teamPlayer.setPlayerId(playerId);
                teamPlayer.setCreatedBy(1);
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

    @Transactional
    public WebServiceResponse deleteTeamById(Integer teamId) {

        try {
            TeamEntity team = teamRepository.findByIdAndDeletedAtIsNull(teamId)
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            team.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            teamRepository.save(team);

            ClubTeamEntity clubTeam = clubTeamRespository.findByTeamIdAndDeletedAtIsNull(teamId)
                    .orElseThrow(() -> new RuntimeException("Team not associated with a club"));
            clubTeam.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            clubTeamRespository.save(clubTeam);

            TeamGenderEntity teamGender = teamGenderRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                    .orElseThrow(() -> new RuntimeException("Team not associated with a Gender"));
            teamGender.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            teamGenderRepository.save(teamGender);

            TeamSubEntity teamSub = teamSubRepository.findByTeamIdAndDeletedAtIsNull(teamId)
                    .orElseThrow(() -> new RuntimeException("Team not associated with a Sub"));
            teamSub.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            teamSubRepository.save(teamSub);
            return new WebServiceResponse(true, "Team deleted successfully");
        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
        }

    }

    @Transactional
    public WebServiceResponse updateTeam (TeamDTO teamDTO, Integer teamId) {
        try {
            TeamEntity team = teamRepository.findById(teamId)
                    .orElseThrow(() -> new RuntimeException("Team not found"));

            team.setName(teamDTO.getTeamName());
            team.setCoach(teamDTO.getCoachName());
            team.setCreatedBy(1);            
            teamRepository.save(team);

            Optional<GenderEntity> gender = genderRepository.findById(teamDTO.getGenderId());            
            Optional<SubEntity> sub = subsRepository.findById(teamDTO.getSubId());

            if (!gender.isPresent() || !sub.isPresent()) {
                throw new RuntimeException("Gender or Sub not found");
            }

            TeamGenderEntity teamGender = teamGenderRepository.findByTeamIdAndDeletedAtIsNull(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
            teamGender.setGenderId(teamDTO.getGenderId());
            teamGender.setCreatedBy(1);
            teamGenderRepository.save(teamGender);

            TeamSubEntity teamSub = teamSubRepository.findByTeamIdAndDeletedAtIsNull(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
            teamSub.setSubId(teamDTO.getSubId());
            teamSub.setCreatedBy(1);
            teamSubRepository.save(teamSub);

            return new WebServiceResponse(true, "Team updated successfully");
        } catch (Exception e) {
            return new WebServiceResponse(false, e.getMessage());
        }
    }

}

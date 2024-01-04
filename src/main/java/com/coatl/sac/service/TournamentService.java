package com.coatl.sac.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coatl.sac.dto.WebServiceResponse;
//import com.coatl.sac.entity.TeamEntity;
import com.coatl.sac.entity.TournamentEntity;
import com.coatl.sac.entity.TournamentGameEntity;
import com.coatl.sac.model.TournamentDTO;
import com.coatl.sac.repository.TeamRepository;
import com.coatl.sac.repository.TournamentGameRepository;
import com.coatl.sac.repository.TournamentRepository;

import jakarta.transaction.Transactional;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentGameRepository tournamentGameRepository;

    @Transactional
    public WebServiceResponse createTournament(TournamentDTO tournamentDto) {
        TournamentEntity tournamentEntity = new TournamentEntity();
        tournamentEntity.setName(tournamentDto.getName());
        //tournamentEntity.setStartDate(Timestamp.valueOf(tournamentDto.getStartDateTime()));
        //tournamentEntity.setEndDate(Timestamp.valueOf(tournamentDto.getEndDateTime()));
        //tournamentEntity.setAgeCategory(tournamentDto.getAgeCategory());
        //tournamentEntity.setGenderCategory(tournamentDto.getGenderCategory());
        tournamentEntity.setCreatedBy(tournamentDto.getUserCreated());
        tournamentRepository.save(tournamentEntity);
        return new WebServiceResponse(true, "Tournament created successfully");
    }

    public List<TournamentEntity> getTournamentList() {
        List<TournamentEntity> allTournaments = tournamentRepository.findAll();
        return allTournaments;
    }

    @Transactional
    public WebServiceResponse addTeamToTournament(Integer tournamentId, Integer gameId) {
        /* TournamentEntity tournamentEntity = */ tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        /* TeamEntity teamEntity = */ teamRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        TournamentGameEntity tournamentGameEntity = new TournamentGameEntity();
        tournamentGameEntity.setTournamentId(tournamentId);
        tournamentGameEntity.setGameId(gameId);
        tournamentGameEntity.setCreatedBy(1);
        tournamentGameRepository.save(tournamentGameEntity);
        return new WebServiceResponse(true, "Team added to tournament successfully");
    }

    public List<Map<String,Object>> getTournamentGames(Integer tournamentId) {
        List<Map<String, Object>> tournamentGames = tournamentRepository.getTournamentGames(tournamentId);
        return tournamentGames;
    }

}

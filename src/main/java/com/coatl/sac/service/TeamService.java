package com.coatl.sac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coatl.sac.repository.TeamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

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
}

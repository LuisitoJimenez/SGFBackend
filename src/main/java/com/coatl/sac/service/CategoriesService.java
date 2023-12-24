package com.coatl.sac.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.coatl.sac.repository.SubsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final SubsRepository subsRepository;

    public List<Map<String, Object>> getSubList() {
        List<Map<String, Object>> allSubs = subsRepository.getSubsList();
        return allSubs;
    }

}

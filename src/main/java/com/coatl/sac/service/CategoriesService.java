package com.coatl.sac.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.coatl.sac.repository.SubRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final SubRepository subsRepository;

    public List<Map<String, Object>> getSubList() {
        List<Map<String, Object>> allSubs = subsRepository.getSubsList();
        return allSubs;
    }

}

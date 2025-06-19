package com.businessArea.businessArea.service;

import com.businessArea.businessArea.dto.theme.ThemeLclsDto;
import com.businessArea.businessArea.repository.IndsLclsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThemeService {

    private final IndsLclsRepository indsLclsRepository;

    public List<ThemeLclsDto> getAllThemes() {
        // 1. Repository의 Fetch Join 쿼리를 호출하여 모든 데이터를 한 번에 조회
        return indsLclsRepository.findAllWithScls().stream()
                // 2. 조회된 엔티티 목록을 DTO 목록으로 변환
                .map(ThemeLclsDto::new)
                .collect(Collectors.toList());
    }
}
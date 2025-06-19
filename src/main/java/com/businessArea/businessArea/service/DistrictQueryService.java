package com.businessArea.businessArea.service;

import com.businessArea.businessArea.dto.response.SidoListDto;
import com.businessArea.businessArea.repository.SidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 프론트엔드에 전체 시도-시군구-행정동 목록을 제공하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DistrictQueryService {

    private final SidoRepository sidoRepository;

    /**
     * 모든 지역 정보를 계층 구조 DTO로 변환하여 반환합니다.
     * @return List<SidoListDto>
     */
    public List<SidoListDto> getAllDistricts() {
        // Repository의 Fetch Join 쿼리를 호출하여 모든 지역 정보를 한번에 가져옵니다.
        return sidoRepository.findAllWithSigunguAndAdong().stream()
                .map(SidoListDto::new)
                .collect(Collectors.toList());
    }
}
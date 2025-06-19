package com.businessArea.businessArea.controller;

import com.businessArea.businessArea.dto.response.SidoListDto;
import com.businessArea.businessArea.service.DistrictQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictQueryService districtQueryService;

    /**
     * 프론트엔드에서 사용할 전체 시도-시군구-행정동 목록을 계층 구조로 반환합니다.
     * @return List<SidoListDto>
     */
    @GetMapping("/all")
    public ResponseEntity<List<SidoListDto>> getAllDistricts() {
        return ResponseEntity.ok(districtQueryService.getAllDistricts());
    }
}
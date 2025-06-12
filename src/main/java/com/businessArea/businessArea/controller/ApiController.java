package com.businessArea.businessArea.controller;

import com.businessArea.businessArea.dto.AdongResponseDto;
import com.businessArea.businessArea.dto.SidoResponseDto;
import com.businessArea.businessArea.dto.SigunguResponseDto;
import com.businessArea.businessArea.service.DistrictInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ApiController {

    private final DistrictInfoService districtInfoService;

    @Autowired
    public ApiController(DistrictInfoService districtInfoService) {
        this.districtInfoService = districtInfoService;
    }

    /**
     * DB에 저장된 모든 시도 목록을 반환합니다.
     * 호출 예시: /api/sido
     */
    @GetMapping("/api/sido")
    public ResponseEntity<List<SidoResponseDto>> getAllSidos() {
        List<SidoResponseDto> sidos = districtInfoService.getAllSidos();
        return ResponseEntity.ok(sidos);
    }

    /**
     * 특정 시도 코드(sidoCode)에 해당하는 모든 시군구 목록을 반환합니다.
     * 호출 예시: /api/sigungu?sidoCode=11  (11: 서울특별시)
     */
    @GetMapping("/api/sigungu")
    public ResponseEntity<List<SigunguResponseDto>> getSigungusBySido(@RequestParam String sidoCode) {
        List<SigunguResponseDto> sigungus = districtInfoService.getSigungusBySidoCode(sidoCode);
        return ResponseEntity.ok(sigungus);
    }

    /**
     * 특정 시군구 코드(sigunguCode)에 해당하는 모든 행정동 목록을 반환합니다.
     * 호출 예시: /api/adong?sigunguCode=11680 (11680: 강남구)
     */
    @GetMapping("/api/adong")
    public ResponseEntity<List<AdongResponseDto>> getAdongsBySigungu(@RequestParam String sigunguCode) {
        List<AdongResponseDto> adongs = districtInfoService.getAdongsBySigunguCode(sigunguCode);
        return ResponseEntity.ok(adongs);
    }


    /**
     * [관리자용] 외부 API를 호출하여 전국 행정구역 데이터를 DB에 저장/동기화합니다.
     */
    @GetMapping("/api/save-districts")
    public String saveAllDistricts() {
        try {
            districtInfoService.saveAllDistrictData();
            return "전국 행정구역 데이터 저장이 성공적으로 완료되었습니다.";
        } catch (IOException e) {
            e.printStackTrace();
            return "데이터 저장 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}

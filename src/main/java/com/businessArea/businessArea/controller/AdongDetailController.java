package com.businessArea.businessArea.controller;

import com.businessArea.businessArea.dto.response.ComprehensiveAdongDto;
import com.businessArea.businessArea.service.AdongDetailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adong-details")
@RequiredArgsConstructor
public class AdongDetailController {

    private final AdongDetailService adongDetailService;

    /**
     * 특정 행정동의 상세 정보(학교, 경계, 업종 통계 등)를 조회합니다.
     * @param adongCode 행정동 코드 (URL 경로)
     * @param themeCode 업종 소분류 코드 (쿼리 파라미터)
     * @return ComprehensiveAdongDto
     * `http://localhost:8080/adong-details/${adongCode}?themeCode=${themeCode}`으로 요청받음
     */
    @GetMapping("/{adongCode}")
    public ResponseEntity<?> getAdongDetails(
            @PathVariable String adongCode,
            @RequestParam String themeCode) {
        try {
            ComprehensiveAdongDto responseDto = adongDetailService.getAdongDetails(adongCode, themeCode);
            return ResponseEntity.ok(responseDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터 조회 중 오류가 발생했습니다.");
        }
    }
}
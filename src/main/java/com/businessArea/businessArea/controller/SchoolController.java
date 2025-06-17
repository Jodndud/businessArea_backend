package com.businessArea.businessArea.controller;

import com.businessArea.businessArea.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/schools") // 학교 관련 API는 이 경로를 사용
public class SchoolController {

    private final SchoolService schoolService;

    /**
     * 모든 행정동의 학교 데이터를 DB에 저장/업데이트하는 API
     */
    @PostMapping("/update")
    public ResponseEntity<String> updateAllSchools() {
        try {
            schoolService.fetchAndSaveSchools();
            return ResponseEntity.ok("학교 데이터 업데이트를 성공적으로 완료했습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("학교 데이터 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 학교 학생 수 정보를 업데이트합니다.
     */
    @PostMapping("/update/school-student-counts")
    public String updateSchoolStudentCounts() {
        try {
            schoolService.updateStudentCounts();
            return "학교별 학생 수 정보 업데이트가 시작되었습니다. 서버 로그를 확인하세요.";
        } catch (Exception e) {
            e.printStackTrace();
            return "업데이트 작업 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
package com.businessArea.businessArea.controller;

import com.businessArea.businessArea.service.IndustryCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/industry")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryCodeService industryCodeService;

    @PostMapping("/save-lcls")
    public String saveLcls() {
        try {
            industryCodeService.fetchAndSaveLcls();
            return "[1단계] 대분류 업종 코드 저장을 완료했습니다.";
        } catch (Exception e) {
            log.error("대분류 업종 코드 저장 중 오류 발생", e);
            return "대분류 업종 코드 저장 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    @PostMapping("/save-mcls")
    public String saveMcls() {
        try {
            industryCodeService.fetchAndSaveMcls();
            return "[2단계] 중분류 업종 코드 저장을 완료했습니다.";
        } catch (Exception e) {
            log.error("중분류 업종 코드 저장 중 오류 발생", e);
            return "중분류 업종 코드 저장 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    @PostMapping("/save-scls")
    public String saveScls() {
        try {
            industryCodeService.fetchAndSaveScls();
            return "[3단계] 소분류 업종 코드 저장을 완료했습니다.";
        } catch (Exception e) {
            log.error("소분류 업종 코드 저장 중 오류 발생", e);
            return "소분류 업종 코드 저장 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
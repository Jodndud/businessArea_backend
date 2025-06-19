package com.businessArea.businessArea.controller;

import com.businessArea.businessArea.dto.theme.ThemeLclsDto;
import com.businessArea.businessArea.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    /**
     * 모든 업종 분류를 계층 구조로 조회합니다.
     */
    @GetMapping("/theme_code/all")
    public ResponseEntity<List<ThemeLclsDto>> getAllThemes() {
        List<ThemeLclsDto> themes = themeService.getAllThemes();
        return ResponseEntity.ok(themes);
    }
}
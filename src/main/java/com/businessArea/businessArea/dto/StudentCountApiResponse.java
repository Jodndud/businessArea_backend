package com.businessArea.businessArea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 학생 수 API 응답의 전체 구조를 나타내는 DTO
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentCountApiResponse {
    // API 응답의 실제 데이터 리스트는 "list" 키를 가집니다.
    private List<StudentCountItemDto> list;
}
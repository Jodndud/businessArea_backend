package com.businessArea.businessArea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolInfoApiResponse {
    // API 응답에서 실제 데이터 리스트는 "list" 라는 키를 가집니다.
    private List<SchoolInfoItemDto> list;
}
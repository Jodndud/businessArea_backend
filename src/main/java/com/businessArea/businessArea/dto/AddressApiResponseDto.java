package com.businessArea.businessArea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressApiResponseDto {
    private String errCd; // 에러 코드 ("0" 이면 성공)
    private String errMsg;
    private List<AddressItemDto> result;
}
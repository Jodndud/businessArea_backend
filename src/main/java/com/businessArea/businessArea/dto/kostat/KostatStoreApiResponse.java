package com.businessArea.businessArea.dto.kostat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KostatStoreApiResponse {
    // 실제 데이터는 "result" 라는 키 안에 리스트로 들어옵니다.
    @JsonProperty("result")
    private List<StoreStatsItemDto> result;
}
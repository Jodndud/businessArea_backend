// 📁 dto/industry/ApiBody.java
package com.businessArea.businessArea.dto.industry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiBody<T> {

    // ✅ 'ApiItems<T>'가 아닌 'List<T>'를 직접 사용하고,
    // JSON의 "items" 키와 매핑되도록 수정합니다.
    @JsonProperty("items")
    private List<T> itemList;

    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
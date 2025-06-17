package com.businessArea.businessArea.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressItemDto {

    @JsonProperty("cd") // 주소 코드
    private String cd;

    @JsonProperty("addr_name") // 주소 이름
    private String addrName;

    @JsonProperty("full_addr") // 전체 주소 (필요시 사용)
    private String fullAddr;
}
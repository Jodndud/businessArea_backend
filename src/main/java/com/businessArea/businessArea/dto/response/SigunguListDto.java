// SigunguListDto.java
package com.businessArea.businessArea.dto.response;

import com.businessArea.businessArea.domain.Sigungu;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SigunguListDto {
    private String sigunguCode;
    private String sigunguName;
    private List<AdongListDto> adongs;

    public SigunguListDto(Sigungu sigungu) {
        this.sigunguCode = sigungu.getCd();
        this.sigunguName = sigungu.getAddrName();
        this.adongs = sigungu.getAdongs().stream().map(AdongListDto::new).collect(Collectors.toList());
    }
}
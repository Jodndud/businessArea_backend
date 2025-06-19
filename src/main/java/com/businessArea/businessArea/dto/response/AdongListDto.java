// AdongListDto.java
package com.businessArea.businessArea.dto.response;

import com.businessArea.businessArea.domain.Adong;
import lombok.Getter;

@Getter
public class AdongListDto {
    private String adongCode;
    private String adongName;

    public AdongListDto(Adong adong) {
        this.adongCode = adong.getCd();
        this.adongName = adong.getAddrName();
    }
}
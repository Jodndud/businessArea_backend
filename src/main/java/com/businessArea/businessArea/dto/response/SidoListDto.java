// SidoListDto.java
package com.businessArea.businessArea.dto.response;

import com.businessArea.businessArea.domain.Sido;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SidoListDto {
    private String sidoCode;
    private String sidoName;
    private List<SigunguListDto> sigungus;

    public SidoListDto(Sido sido) {
        this.sidoCode = sido.getCd();
        this.sidoName = sido.getAddrName();
        this.sigungus = sido.getSigungus().stream().map(SigunguListDto::new).collect(Collectors.toList());
    }
}
package com.businessArea.businessArea.service;

import com.businessArea.businessArea.domain.Adong;
import com.businessArea.businessArea.domain.IndsScls;
import com.businessArea.businessArea.dto.kostat.KostatStoreApiResponse;
import com.businessArea.businessArea.dto.kostat.StoreStatsItemDto;
import com.businessArea.businessArea.dto.response.ComprehensiveAdongDto;
import com.businessArea.businessArea.dto.response.StoreStatsDto;
import com.businessArea.businessArea.repository.AdongRepository;
import com.businessArea.businessArea.repository.IndsSclsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdongDetailService {

    private final AdongRepository adongRepository;
    private final KostatAuthService kostatAuthService;
    private final WebClient webClient;
    private final IndsSclsRepository indsSclsRepository; // ✅ Repository 주입 추가

    private static final String API_URL = "https://sgisapi.kostat.go.kr/OpenAPI3/stats/company.json";

    @Transactional(readOnly = true)
    public ComprehensiveAdongDto getAdongDetails(String adongCode, String themeCode) {

        Adong adong = adongRepository.findByCdWithDetails(adongCode)
                .orElseThrow(() -> new EntityNotFoundException("행정동 코드 " + adongCode + "를 찾을 수 없습니다."));

        // 1. themeCode를 사용해 DB에서 업종 소분류 정보 조회
        Optional<IndsScls> sclsOptional = indsSclsRepository.findByIndsSclsCd(themeCode);
        String themeNameFromDb = sclsOptional.map(IndsScls::getIndsSclsNm).orElse(null); // 있으면 이름을, 없으면 null을 할당

        // 2. 통계청 API 호출
        String accessToken = kostatAuthService.getAccessToken();
        KostatStoreApiResponse apiResponse = webClient.get()
                .uri(API_URL, uriBuilder -> uriBuilder
                        .queryParam("accessToken", accessToken)
                        .queryParam("adm_cd", adong.getCd())
                        .queryParam("theme_cd", themeCode)
                        .queryParam("year", "2023")
                        .build())
                .retrieve()
                .bodyToMono(KostatStoreApiResponse.class)
                .block();

        StoreStatsItemDto statsItem = (apiResponse != null && apiResponse.getResult() != null && !apiResponse.getResult().isEmpty())
                ? apiResponse.getResult().get(0)
                : new StoreStatsItemDto();

        // 3. DTO를 생성할 때 DB에서 조회한 업종명을 함께 전달
        StoreStatsDto finalStatsDto = new StoreStatsDto(statsItem, themeNameFromDb);

        // 4. 최종 응답 DTO 생성
        return new ComprehensiveAdongDto(adong, finalStatsDto);
    }

}

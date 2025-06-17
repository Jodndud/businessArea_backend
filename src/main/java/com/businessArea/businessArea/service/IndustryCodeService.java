package com.businessArea.businessArea.service;

import com.businessArea.businessArea.domain.IndsLcls;
import com.businessArea.businessArea.domain.IndsMcls;
import com.businessArea.businessArea.domain.IndsScls;
import com.businessArea.businessArea.dto.industry.*;
import com.businessArea.businessArea.repository.IndsLclsRepository;
import com.businessArea.businessArea.repository.IndsMclsRepository;
import com.businessArea.businessArea.repository.IndsSclsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndustryCodeService {

    private final IndsLclsRepository indsLclsRepository;
    private final IndsMclsRepository indsMclsRepository;
    private final IndsSclsRepository indsSclsRepository;
    private final WebClient webClient;

    @Value("${data.go.kr.serviceKey}")
    private String serviceKey;
    @Value("${data.go.kr.authKey}")
    private String authorizationKey;

    private static final String BASE_URL = "https://apis.data.go.kr/B553077/api/open/sdsc2";
    private static final int ROWS_PER_PAGE = 1000;

    /**
     * [1단계] 대분류 업종 데이터를 가져와 저장합니다.
     * Controller에서 호출할 수 있도록 public으로 변경합니다.
     */
    @Transactional
    public void fetchAndSaveLcls() {
        log.info("[1단계] 업종 코드 데이터베이스 초기화 및 대분류 저장을 시작합니다.");
        indsSclsRepository.deleteAllInBatch();
        indsMclsRepository.deleteAllInBatch();
        indsLclsRepository.deleteAllInBatch();

        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

        ApiResponse<LclsItemDto> response = webClient.get()
                .uri(BASE_URL + "/largeUpjongList", uriBuilder -> uriBuilder
                        .queryParam("serviceKey", encodedServiceKey)
                        .queryParam("type", "json")
                        .queryParam("numOfRows", 100)
                        .build())
                .header("Authorization", authorizationKey)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<LclsItemDto>>() {})
                .block();

        List<IndsLcls> lclsList = new ArrayList<>();
        if (response != null && response.getBody() != null && response.getBody().getItemList() != null) {
            for (LclsItemDto item : response.getBody().getItemList()) {
                lclsList.add(new IndsLcls(item.getIndsLclsCd(), item.getIndsLclsNm()));
            }
        }
        indsLclsRepository.saveAll(lclsList);
        log.info("[1단계] 대분류 코드 {}건 저장 완료.", lclsList.size());
    }

    /**
     * [2단계] 중분류 업종 데이터를 가져와 저장합니다.
     * Controller에서 호출할 수 있도록 public으로 변경합니다.
     */
    @Transactional
    public void fetchAndSaveMcls() {
        log.info("[2단계] 중분류 저장을 시작합니다.");
        List<IndsLcls> lclsList = indsLclsRepository.findAll();
        if (lclsList.isEmpty()) {
            log.warn("저장된 대분류 데이터가 없습니다. 먼저 대분류 저장을 실행해주세요.");
            return;
        }
        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

        List<IndsMcls> mclsListToSave = new ArrayList<>();
        for (IndsLcls lcls : lclsList) {
            ApiResponse<MclsItemDto> response = webClient.get()
                    .uri(BASE_URL + "/middleUpjongList", uriBuilder -> uriBuilder
                            .queryParam("serviceKey", encodedServiceKey)
                            .queryParam("indsLclsCd", lcls.getIndsLclsCd())
                            .queryParam("type", "json")
                            .queryParam("numOfRows", 100)
                            .build())
                    .header("Authorization", authorizationKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<MclsItemDto>>() {})
                    .block();

            if (response != null && response.getBody() != null && response.getBody().getItemList() != null) {
                for (MclsItemDto item : response.getBody().getItemList()) {
                    mclsListToSave.add(new IndsMcls(item.getIndsMclsCd(), item.getIndsMclsNm(), lcls));
                }
            }
        }
        indsMclsRepository.saveAll(mclsListToSave);
        log.info("[2단계] 중분류 코드 {}건 저장 완료.", mclsListToSave.size());
    }

    /**
     * [3단계] 소분류 업종 데이터를 가져와 저장합니다.
     * Controller에서 호출할 수 있도록 public으로 변경합니다.
     */
    @Transactional
    public void fetchAndSaveScls() {
        log.info("[3단계] 소분류 저장을 시작합니다.");
        List<IndsMcls> mclsList = indsMclsRepository.findAll();
        if (mclsList.isEmpty()) {
            log.warn("저장된 중분류 데이터가 없습니다. 먼저 중분류 저장을 실행해주세요.");
            return;
        }
        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

        List<IndsScls> sclsListToSave = new ArrayList<>();
        for (IndsMcls mcls : mclsList) {
            int pageNo = 1;
            while (true) {
                final int currentPageNo = pageNo;
                URI uri = UriComponentsBuilder
                        .fromHttpUrl(BASE_URL + "/smallUpjongList")
                        .queryParam("serviceKey", encodedServiceKey)
                        .queryParam("indsMclsCd", mcls.getIndsMclsCd())
                        .queryParam("type", "json")
                        .queryParam("pageNo", currentPageNo)
                        .queryParam("numOfRows", ROWS_PER_PAGE)
                        .build(true)
                        .toUri();

                log.info("Requesting SCLS for mclsCd='{}' with URL: {}", mcls.getIndsMclsCd(), uri);

                try {
                    ApiResponse<SclsItemDto> response = webClient.get()
                            .uri(uri)
                            .header("Authorization", authorizationKey)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<ApiResponse<SclsItemDto>>() {})
                            .block();

                    if (response == null || response.getBody() == null || response.getBody().getItemList() == null || response.getBody().getItemList().isEmpty()) {
                        break;
                    }
                    for (SclsItemDto item : response.getBody().getItemList()) {
                        sclsListToSave.add(new IndsScls(item.getIndsSclsCd(), item.getIndsSclsNm(), mcls));
                    }
                    if (currentPageNo * ROWS_PER_PAGE >= response.getBody().getTotalCount()) {
                        break;
                    }
                    pageNo++;
                } catch (Exception e) {
                    log.error("소분류 조회 중 '{}' ({}) 에서 오류 발생. 다음 중분류 코드로 넘어갑니다. 에러: {}",
                            mcls.getIndsMclsNm(), mcls.getIndsMclsCd(), e.getMessage());
                    break;
                }
            }
        }
        indsSclsRepository.saveAll(sclsListToSave);
        log.info("[3단계] 소분류 코드 {}건 저장 완료.", sclsListToSave.size());
    }
}
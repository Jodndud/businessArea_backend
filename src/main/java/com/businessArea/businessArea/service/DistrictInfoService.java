package com.businessArea.businessArea.service;

import com.businessArea.businessArea.domain.Adong;
import com.businessArea.businessArea.domain.Sido;
import com.businessArea.businessArea.domain.Sigungu;
import com.businessArea.businessArea.dto.AdongResponseDto;
import com.businessArea.businessArea.dto.SidoResponseDto;
import com.businessArea.businessArea.dto.SigunguResponseDto;
import com.businessArea.businessArea.repository.AdongRepository;
import com.businessArea.businessArea.repository.SidoRepository;
import com.businessArea.businessArea.repository.SigunguRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictInfoService {

    private static final Logger log = LoggerFactory.getLogger(DistrictInfoService.class);

    // --- 의존성 주입 ---
    @Value("${api.serviceKey}")
    private String serviceKey;
    private final String BASE_URL = "http://apis.data.go.kr/B553077/api/open/sdsc2";
    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;
    private final AdongRepository adongRepository;
    private final ObjectMapper objectMapper;

    public DistrictInfoService(SidoRepository sidoRepository, SigunguRepository sigunguRepository, AdongRepository adongRepository, ObjectMapper objectMapper) {
        this.sidoRepository = sidoRepository;
        this.sigunguRepository = sigunguRepository;
        this.adongRepository = adongRepository;
        this.objectMapper = objectMapper;
    }

    // --- 프론트엔드 제공용 조회 API 로직 ---

    /**
     * DB에 저장된 모든 시도 목록을 조회하여 DTO 리스트로 반환합니다.
     */
    public List<SidoResponseDto> getAllSidos() {
        return sidoRepository.findAll().stream()
                .map(sido -> new SidoResponseDto(sido.getCtprvnCd(), sido.getCtprvnNm()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 시도 코드(sidoCode)에 해당하는 모든 시군구 목록을 조회하여 DTO 리스트로 반환합니다.
     */
    public List<SigunguResponseDto> getSigungusBySidoCode(String sidoCode) {
        return sidoRepository.findByCtprvnCd(sidoCode)
                .map(sido -> sigunguRepository.findAllBySido(sido).stream()
                        .map(sigungu -> new SigunguResponseDto(sigungu.getSigunguCd(), sigungu.getSigunguNm()))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    /**
     * 특정 시군구 코드(sigunguCode)에 해당하는 모든 행정동 목록을 조회하여 DTO 리스트로 반환합니다.
     */
    public List<AdongResponseDto> getAdongsBySigunguCode(String sigunguCode) {
        return sigunguRepository.findBySigunguCd(sigunguCode)
                .map(sigungu -> adongRepository.findAllBySigungu(sigungu).stream()
                        .map(adong -> new AdongResponseDto(adong.getAdongCd(), adong.getAdongNm()))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    // --- 데이터 저장/동기화 로직 ---

    /**
     * API를 호출하여 데이터베이스에 없는 새로운 행정구역 정보만 저장(동기화)합니다.
     */
    @Transactional
    public void saveAllDistrictData() throws IOException {
        log.info("=== 전국 행정구역 데이터 동기화를 시작합니다. ===");

        List<Sido> sidos = findOrCreateSidos();
        log.info(">> {}개의 시도 정보 동기화 완료.", sidos.size());

        for (Sido sido : sidos) {
            List<Sigungu> sigungus = findOrCreateSigungus(sido);
            for (Sigungu sigungu : sigungus) {
                findOrCreateAdongs(sigungu);
            }
        }
        log.info("=== 모든 행정구역 데이터 동기화가 완료되었습니다. ===");
    }

    private List<Sido> findOrCreateSidos() throws IOException {
        String jsonString = callBaroApi("mega", null);
        JsonNode items = parseItems(jsonString);
        List<Sido> resultSidos = new ArrayList<>();

        if (items != null && items.isArray()) {
            for (JsonNode item : items) {
                String ctprvnCd = item.path("ctprvnCd").asText();
                String ctprvnNm = item.path("ctprvnNm").asText();
                Sido sido = sidoRepository.findByCtprvnCd(ctprvnCd)
                        .orElseGet(() -> sidoRepository.save(new Sido(ctprvnCd, ctprvnNm)));
                resultSidos.add(sido);
            }
        }
        return resultSidos;
    }

    private List<Sigungu> findOrCreateSigungus(Sido sido) throws IOException {
        String jsonString = callBaroApi("cty", sido.getCtprvnCd());
        JsonNode items = parseItems(jsonString);
        List<Sigungu> resultSigungus = new ArrayList<>();

        if (items != null && items.isArray()) {
            for (JsonNode item : items) {
                String sigunguCd = item.path("signguCd").asText();
                String sigunguNm = item.path("signguNm").asText();
                Sigungu sigungu = sigunguRepository.findBySigunguCd(sigunguCd)
                        .orElseGet(() -> sigunguRepository.save(new Sigungu(sigunguCd, sigunguNm, sido)));
                resultSigungus.add(sigungu);
            }
        }
        log.info("  - {}: {}개 시군구 동기화 완료.", sido.getCtprvnNm(), resultSigungus.size());
        return resultSigungus;
    }

    private void findOrCreateAdongs(Sigungu sigungu) throws IOException {
        String jsonString = callBaroApi("admi", sigungu.getSigunguCd());
        JsonNode items = parseItems(jsonString);

        if (items != null && items.isArray()) {
            List<Adong> adongsToSave = new ArrayList<>();
            for (JsonNode item : items) {
                String adongCd = item.path("adongCd").asText();
                if (adongRepository.findByAdongCd(adongCd).isEmpty()) {
                    adongsToSave.add(new Adong(item.path("adongCd").asText(), item.path("adongNm").asText(), sigungu));
                }
            }
            if (!adongsToSave.isEmpty()) {
                adongRepository.saveAll(adongsToSave);
                log.info("    -- {}: {}개 행정동 새로 저장.", sigungu.getSigunguNm(), adongsToSave.size());
            }
        }
    }

    // --- API 호출 및 JSON 파싱 공통 메소드 ---

    private String callBaroApi(String catId, String code) {
        WebClient webClient = WebClient.create();
        return webClient.get()
                .uri(BASE_URL, uriBuilder -> {
                    uriBuilder.path("/baroApi")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("resId", "dong")
                            .queryParam("catId", catId)
                            .queryParam("numOfRows", "200")
                            .queryParam("type", "json");
                    if ("cty".equals(catId)) {
                        uriBuilder.queryParam("ctprvnCd", code);
                    } else if ("admi".equals(catId)) {
                        uriBuilder.queryParam("signguCd", code);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private JsonNode parseItems(String jsonString) throws IOException {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        try {
            JsonNode root = objectMapper.readTree(jsonString);
            return root.path("body").path("items");
        } catch (JsonProcessingException e) {
            log.error("### JSON 파싱 실패! API 서버가 에러를 반환했을 수 있습니다. ###");
            log.error("--- API 서버 원본 응답 --- \n{}", jsonString);
            return null;
        }
    }
}

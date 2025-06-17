package com.businessArea.businessArea.service;

import com.businessArea.businessArea.domain.Adong;
import com.businessArea.businessArea.domain.School;
import com.businessArea.businessArea.dto.SchoolInfoApiResponse;
import com.businessArea.businessArea.dto.SchoolInfoItemDto;
import com.businessArea.businessArea.dto.StudentCountApiResponse;
import com.businessArea.businessArea.dto.StudentCountItemDto;
import com.businessArea.businessArea.repository.AdongRepository;
import com.businessArea.businessArea.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final AdongRepository adongRepository;
    private final WebClient webClient; // Bean으로 주입

    @Value("${schoolinfo.apiKey}")
    private String apiKey;

    /**
     * [기존 메서드] 학교 기본 정보 전체 저장
     */
    @Transactional
    public void fetchAndSaveSchools() {
        log.info("학교 데이터 업데이트를 시작합니다. (학교알리미 API)");

        Map<String, Adong> adongMap = adongRepository.findAll().stream()
                .collect(Collectors.toMap(this::buildFullAddressKey, Function.identity(), (adong1, adong2) -> adong1));
        log.info("주소 데이터 {}건을 메모리에 로드했습니다.", adongMap.size());

        schoolRepository.deleteAllInBatch();
        List<School> totalSchoolsToSave = new ArrayList<>();

        List<String> schoolKinds = List.of("02", "03", "04", "05", "06", "07");

        for (String kindCode : schoolKinds) {
            log.info("학교급 코드 '{}' 조회 시작...", kindCode);
            try {
                SchoolInfoApiResponse response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("www.schoolinfo.go.kr")
                                .path("/openApi.do")
                                .queryParam("apiKey", apiKey)
                                .queryParam("apiType", "0")
                                .queryParam("schulKndCode", kindCode)
                                .build())
                        .retrieve()
                        .bodyToMono(SchoolInfoApiResponse.class)
                        .block();

                if (response != null && response.getList() != null) {
                    for (SchoolInfoItemDto item : response.getList()) {
                        Adong matchedAdong = findAdongFromAddress(adongMap, item.getAdresBrkdn());

                        if (matchedAdong != null) {
                            School school = new School(
                                    matchedAdong,
                                    item.getAdresBrkdn(),
                                    item.getSchulNm(),
                                    item.getSchulCrseScValueNm(),
                                    item.getSchulCode()
                            );
                            totalSchoolsToSave.add(school);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("학교급 코드 '{}' 조회 중 오류 발생: {}", kindCode, e.getMessage());
            }
        }

        schoolRepository.saveAll(totalSchoolsToSave);
        log.info("총 {}개의 학교 데이터를 저장했습니다.", totalSchoolsToSave.size());
    }

    /**
     * [추가된 메서드] 학생 수 정보 업데이트
     */
    @Transactional
    public void updateStudentCounts() {
        log.info("학교별 학생 수 업데이트를 시작합니다.");

        Map<String, Integer> studentCountMap = new HashMap<>();
        List<String> schoolKinds = List.of("02", "03", "04", "05", "06", "07");
        String year = "2023";

        for (String kindCode : schoolKinds) {
            log.info("학교급 코드 '{}', 공시년도 '{}' 학생 수 조회 중...", kindCode, year);
            try {
                StudentCountApiResponse response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("www.schoolinfo.go.kr")
                                .path("/openApi.do")
                                .queryParam("apiKey", apiKey)
                                .queryParam("apiType", "62")
                                .queryParam("pbanYr", year)
                                .queryParam("schulKndCode", kindCode)
                                .build())
                        .retrieve()
                        .bodyToMono(StudentCountApiResponse.class)
                        .block();

                if (response != null && response.getList() != null) {
                    for (StudentCountItemDto item : response.getList()) {
                        Integer studentCount = parseColFgrSum(item.getColFgrSum());
                        if (studentCount != null) {
                            studentCountMap.put(item.getSchulCode(), studentCount);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("학생 수 조회 중 오류 발생 (학교급: {}): {}", kindCode, e.getMessage());
            }
        }
        log.info("API로부터 {}개의 학교 학생 수 정보를 가져왔습니다.", studentCountMap.size());

        List<School> allSchools = schoolRepository.findAll();
        int updateCount = 0;

        for (School school : allSchools) {
            Integer count = studentCountMap.get(school.getSchulCode());
            if (count != null) {
                school.setColFgrSum(count);
                updateCount++;
            }
        }

        schoolRepository.saveAll(allSchools);
        log.info("총 {}개의 학교 데이터 중 {}개의 학생 수 정보를 업데이트했습니다.", allSchools.size(), updateCount);
    }

    // [기존 헬퍼 메서드]
    private Adong findAdongFromAddress(Map<String, Adong> adongMap, String fullAddress) {
        if (fullAddress == null || fullAddress.trim().isEmpty()) {
            return null;
        }
        String[] parts = fullAddress.split(" ");
        if (parts.length >= 3) {
            String key = parts[0] + " " + parts[1] + " " + parts[2];
            return adongMap.get(key);
        }
        return null;
    }

    // [기존 헬퍼 메서드]
    private String buildFullAddressKey(Adong adong) {
        // 이 부분은 Sido, Sigungu 엔티티 구조에 맞게 조정이 필요할 수 있습니다.
        String sidoName = adong.getSigungu().getSido().getAddrName();
        if (sidoName.endsWith("시") && !sidoName.endsWith("특별시") && !sidoName.endsWith("광역시")) {
            sidoName = sidoName.substring(0, sidoName.length() - 1);
        }
        String sigunguName = adong.getSigungu().getAddrName();
        String adongName = adong.getAddrName();
        return sidoName + " " + sigunguName + " " + adongName;
    }

    /**
     * [추가된 헬퍼 메서드]
     * "610(4)" 형태의 문자열에서 숫자 610만 정수로 파싱합니다.
     */
    private Integer parseColFgrSum(String rawSum) {
        if (rawSum == null || rawSum.trim().isEmpty()) {
            return null;
        }
        try {
            int parenthesisIndex = rawSum.indexOf('(');
            if (parenthesisIndex != -1) {
                return Integer.parseInt(rawSum.substring(0, parenthesisIndex));
            }
            return Integer.parseInt(rawSum);
        } catch (NumberFormatException e) {
            log.error("'{}'는 숫자로 변환할 수 없습니다.", rawSum);
            return null;
        }
    }
}
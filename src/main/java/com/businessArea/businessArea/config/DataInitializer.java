package com.businessArea.businessArea.config;

import com.businessArea.businessArea.domain.IndsLcls;
import com.businessArea.businessArea.domain.IndsScls;
import com.businessArea.businessArea.dto.industry.IndustryLargeCategoryDto;
import com.businessArea.businessArea.dto.industry.IndustrySmallCategoryDto;
import com.businessArea.businessArea.repository.IndsLclsRepository;
import com.businessArea.businessArea.repository.IndsSclsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IndsLclsRepository indsLclsRepository;
    private final IndsSclsRepository indsSclsRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // DB에 데이터가 이미 존재하면 실행하지 않음 (중복 저장 방지)
        if (indsLclsRepository.count() > 0) {
            log.info("업종 분류 데이터가 이미 존재하므로 초기화를 건너뜁니다.");
            return;
        }

        log.info("JSON 파일로부터 업종 분류 데이터 초기화를 시작합니다...");

        // 1. JSON 파일을 읽고 파싱하기 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // 2. src/main/resources/data/industry_codes.json 파일 리소스를 가져옴
        ClassPathResource resource = new ClassPathResource("data/industry_codes.json");
        InputStream inputStream = resource.getInputStream();

        // 3. JSON 파일을 DTO 리스트로 변환 (JSON의 최상위가 배열이므로 List로 받음)
        List<IndustryLargeCategoryDto> lclsDataList = objectMapper.readValue(inputStream, new TypeReference<List<IndustryLargeCategoryDto>>() {});

        List<IndsScls> allSclsToSave = new ArrayList<>();

        // 4. DTO 리스트를 순회하며 엔티티 생성
        for (IndustryLargeCategoryDto lclsData : lclsDataList) {
            // 4-1. 대분류 엔티티 생성
            IndsLcls lcls = new IndsLcls(lclsData.getLargeCategoryCode(), lclsData.getLargeCategoryName());
            // 4-2. 부모(대분류)를 먼저 저장해야 자식(소분류)이 참조할 수 있음
            IndsLcls savedLcls = indsLclsRepository.save(lcls);

            // 4-3. 해당 대분류에 속한 소분류 엔티티들 생성
            if (lclsData.getSmallCategories() != null) {
                for (IndustrySmallCategoryDto sclsData : lclsData.getSmallCategories()) {
                    // 저장된 부모(savedLcls)와의 관계를 설정하여 생성
                    IndsScls scls = new IndsScls(sclsData.getSmallCategoryCode(), sclsData.getSmallCategoryName(), savedLcls);
                    allSclsToSave.add(scls);
                }
            }
        }

        // 5. 모든 소분류 데이터를 한번에 저장 (성능 최적화)
        indsSclsRepository.saveAll(allSclsToSave);

        log.info("업종 분류 데이터 초기화가 완료되었습니다. 대분류: {}건, 소분류: {}건", lclsDataList.size(), allSclsToSave.size());
    }
}
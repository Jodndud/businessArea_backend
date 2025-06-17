package com.businessArea.businessArea.service;

import com.businessArea.businessArea.domain.Adong;
import com.businessArea.businessArea.domain.Boundary;
import com.businessArea.businessArea.domain.Sido;
import com.businessArea.businessArea.domain.Sigungu;
import com.businessArea.businessArea.dto.AddressApiResponseDto;
import com.businessArea.businessArea.dto.AddressItemDto;
import com.businessArea.businessArea.dto.GeoJsonDto;
import com.businessArea.businessArea.dto.TokenResponseDto;
import com.businessArea.businessArea.repository.AdongRepository;
import com.businessArea.businessArea.repository.BoundaryRepository;
import com.businessArea.businessArea.repository.SidoRepository;
import com.businessArea.businessArea.repository.SigunguRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    // Repositories
    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;
    private final AdongRepository adongRepository;
    private final BoundaryRepository boundaryRepository;
    private final ObjectMapper objectMapper;

    /**
     * 다른 서비스에서 토큰을 사용할 수 있도록 public 메소드 제공
     */
    public String getAccessTokenForSchoolService() {
        return getAccessToken();
    }

    // SGIS API Keys from application.properties
    @Value("${sgis.consumer-key}")
    private String consumerKey;

    @Value("${sgis.consumer-secret}")
    private String consumerSecret;

    // WebClient for API calls
    private final WebClient webClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024)) // 16MB로 버퍼 크기 증가
                    .build())
            .baseUrl("https://sgisapi.kostat.go.kr")
            .defaultHeader("Content-Type", "application/json")
            .build();


    /**
     * 통계청 SGIS API Access Token을 발급받는 메소드
     */
    private String getAccessToken() {
        String authUrl = "/OpenAPI3/auth/authentication.json";

        TokenResponseDto responseDto = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(authUrl)
                        .queryParam("consumer_key", consumerKey)
                        .queryParam("consumer_secret", consumerSecret)
                        .build())
                .retrieve()
                .bodyToMono(TokenResponseDto.class)
                .block();

        if (responseDto != null && responseDto.getResult() != null) {
            log.info("SGIS Access Token 발급 성공");
            return responseDto.getResult().getAccessToken();
        } else {
            log.error("SGIS Access Token 발급 실패");
            throw new RuntimeException("SGIS Access Token 발급에 실패했습니다.");
        }
    }

    /**
     * 주소 데이터를 단계별로 가져오는 범용 메소드
     */
    private List<AddressItemDto> fetchAddressData(String accessToken, String cd) {
        String addrUrl = "/OpenAPI3/addr/stage.json";

        AddressApiResponseDto response = webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(addrUrl).queryParam("accessToken", accessToken);
                    if (cd != null && !cd.isEmpty()) {
                        uriBuilder.queryParam("cd", cd);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(AddressApiResponseDto.class)
                .block();

        if (response != null && "0".equals(response.getErrCd())) {
            return response.getResult();
        }
        return new ArrayList<>();
    }


    /**
     * 모든 주소 데이터를 가져와 데이터베이스에 저장하는 메인 메소드
     */
    @Transactional
    public void fetchAndSaveAllAddressData() {
        log.info("주소 데이터 업데이트를 시작합니다.");

        boundaryRepository.deleteAllInBatch();
        adongRepository.deleteAllInBatch();
        sigunguRepository.deleteAllInBatch();
        sidoRepository.deleteAllInBatch();
        log.info("기존 주소 및 경계 데이터를 모두 삭제했습니다.");

        String accessToken = getAccessToken();

        log.info("시도 데이터를 가져옵니다...");
        List<AddressItemDto> sidoItems = fetchAddressData(accessToken, null);
        List<Sido> sidoList = new ArrayList<>();
        for (AddressItemDto item : sidoItems) {
            sidoList.add(new Sido(item.getCd(), item.getAddrName()));
        }
        sidoRepository.saveAll(sidoList);
        log.info("{}개의 시도 데이터를 저장했습니다.", sidoList.size());


        log.info("시군구 데이터를 가져옵니다...");
        List<Sigungu> totalSigunguList = new ArrayList<>();
        for (Sido sido : sidoList) {
            List<AddressItemDto> sigunguItems = fetchAddressData(accessToken, sido.getCd());
            for (AddressItemDto item : sigunguItems) {
                totalSigunguList.add(new Sigungu(item.getCd(), item.getAddrName(), sido));
            }
        }
        sigunguRepository.saveAll(totalSigunguList);
        log.info("{}개의 시군구 데이터를 저장했습니다.", totalSigunguList.size());


        log.info("행정동 데이터를 가져옵니다...");
        List<Adong> totalAdongList = new ArrayList<>();
        for (Sigungu sigungu : totalSigunguList) {
            List<AddressItemDto> adongItems = fetchAddressData(accessToken, sigungu.getCd());
            for (AddressItemDto item : adongItems) {
                totalAdongList.add(new Adong(item.getCd(), item.getAddrName(), sigungu));
            }
        }
        adongRepository.saveAll(totalAdongList);
        log.info("{}개의 행정동 데이터를 저장했습니다.", totalAdongList.size());

        log.info("모든 주소 데이터 업데이트가 성공적으로 완료되었습니다.");
    }

    /**
     * 모든 행정동의 경계(Polygon) 데이터를 가져와 GEOMETRY 타입으로 저장하는 메소드
     */
    @Transactional
    public void fetchAndSaveBoundaries() {
        log.info("행정동 경계 데이터 업데이트를 시작합니다. (GEOMETRY 타입)");

        List<Adong> allAdongs = adongRepository.findAll();
        if (allAdongs.isEmpty()) {
            log.warn("행정동 데이터가 없습니다. 주소 데이터부터 저장해주세요.");
            return;
        }

        boundaryRepository.deleteAllInBatch();
        log.info("기존 경계 데이터를 모두 삭제했습니다.");

        String accessToken = getAccessToken();
        String boundaryUrl = "/OpenAPI3/boundary/hadmarea.geojson";

        for (Adong adong : allAdongs) {
            try {
                GeoJsonDto response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path(boundaryUrl)
                                .queryParam("accessToken", accessToken)
                                .queryParam("adm_cd", adong.getCd())
                                .queryParam("year", "2024")
                                .queryParam("low_search", "0")
                                .build())
                        .retrieve()
                        .bodyToMono(GeoJsonDto.class)
                        .block();

                if (response != null && response.getFeatures() != null && !response.getFeatures().isEmpty()) {
                    // Feature에서 geometry 정보 추출
                    com.businessArea.businessArea.dto.GeometryDto geometry = response.getFeatures().get(0).getGeometry();

                    // geometry 타입과 좌표를 새 메소드에 전달
                    Polygon polygon = geoJsonToPolygon(geometry.getType(), geometry.getCoordinates());

                    if (polygon != null) {
                        Boundary boundary = new Boundary(polygon, adong);
                        boundaryRepository.save(boundary);
                        log.info("{} ({}) 경계 데이터 저장 성공 (타입: {})", adong.getAddrName(), adong.getCd(), geometry.getType());
                    }
                }
            } catch (Exception e) {
                log.error("{} ({}) 경계 데이터 저장 중 오류 발생: {}", adong.getAddrName(), adong.getCd(), e.getMessage());
            }
        }
        log.info("행정동 경계 데이터 업데이트를 완료했습니다.");
    }

    /**
     * GeoJSON 좌표계(List<Object>)를 JTS의 Polygon 객체로 변환하는 헬퍼 메소드
     */
    private Polygon geoJsonToPolygon(List<Object> geoJsonCoordinates) {
        TypeReference<List<List<List<Double>>>> typeRef = new TypeReference<>() {};
        List<List<List<Double>>> polygonCoords = objectMapper.convertValue(geoJsonCoordinates, typeRef);

        GeometryFactory geometryFactory = new GeometryFactory();

        List<List<Double>> exteriorRingCoords = polygonCoords.get(0);

        Coordinate[] coordinates = exteriorRingCoords.stream()
                .map(coord -> new Coordinate(coord.get(0), coord.get(1)))
                .toArray(Coordinate[]::new);

        return geometryFactory.createPolygon(coordinates);
    }

    /**
     * GeoJSON 좌표계를 JTS의 Polygon 객체로 변환하는 헬퍼 메소드
     * Polygon과 MultiPolygon 타입을 모두 처리합니다.
     */
    private Polygon geoJsonToPolygon(String type, List<Object> geoJsonCoordinates) {
        GeometryFactory geometryFactory = new GeometryFactory();

        if ("Polygon".equalsIgnoreCase(type)) {
            // 기존 Polygon 처리 로직
            TypeReference<List<List<List<Double>>>> typeRef = new TypeReference<>() {};
            List<List<List<Double>>> polygonCoords = objectMapper.convertValue(geoJsonCoordinates, typeRef);
            List<List<Double>> exteriorRingCoords = polygonCoords.get(0);

            Coordinate[] coordinates = exteriorRingCoords.stream()
                    .map(coord -> new Coordinate(coord.get(0), coord.get(1)))
                    .toArray(Coordinate[]::new);
            return geometryFactory.createPolygon(coordinates);

        } else if ("MultiPolygon".equalsIgnoreCase(type)) {
            // MultiPolygon 처리 로직: 여러 폴리곤 중 가장 큰 첫 번째 폴리곤만 사용
            TypeReference<List<List<List<List<Double>>>>> typeRef = new TypeReference<>() {};
            List<List<List<List<Double>>>> multiPolygonCoords = objectMapper.convertValue(geoJsonCoordinates, typeRef);

            if (multiPolygonCoords.isEmpty() || multiPolygonCoords.get(0).isEmpty()) {
                return null; // 빈 데이터 처리
            }

            List<List<Double>> exteriorRingCoords = multiPolygonCoords.get(0).get(0); // 첫 번째 폴리곤의 외부 링

            Coordinate[] coordinates = exteriorRingCoords.stream()
                    .map(coord -> new Coordinate(coord.get(0), coord.get(1)))
                    .toArray(Coordinate[]::new);
            return geometryFactory.createPolygon(coordinates);
        }

        return null; // 그 외 타입은 처리하지 않음
    }
}
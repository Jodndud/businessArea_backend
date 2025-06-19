<p align="center">
  <img src="./title.png" width="400" alt="자리잡자 프로젝트 타이틀 이미지"/>
</p>
# 📈 자리잡자 (Jariring-Japja)

> 청년 창업자를 위한 빅데이터 기반 상권 분석 및 파워(POWER) 지수 제공 서비스

<br>

![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle&logoColor=white)
![JPA](https://img.shields.io/badge/Spring_Data_JPA-3.x-6DB33F?logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)

<br>

## 1. 프로젝트 소개

**자리잡자**는 성공적인 창업을 꿈꾸는 청년 창업자들을 위한 의사결정 지원 서비스입니다. 복잡한 상권 데이터를 한눈에 파악하기 어려운 문제를 해결하고자, 사용자가 원하는 **지역(행정동)과 업종**을 선택하면 해당 상권의 잠재력을 직관적인 **파워(POWER) 지수**로 알려줍니다.

이를 통해 데이터에 기반한 합리적인 창업 위치 선정을 돕고, 예비 창업자들의 성공 가능성을 높이는 것을 목표로 합니다.

<br>

## 2. 주요 기능

- **📈 상권 파워(POWER) 지수 제공**: 행정동 내 학생 수, 직장인 수, 동종 업계 수를 종합 분석하여 독자적인 상권 파워 지수 산출
- **🗺️ 지도 기반 시각화**: 분석된 데이터를 지도 위에 시각적으로 표현하여 사용자의 이해를 도움 (클라이언트 파트)
- **📊 데이터 조회 API**: 행정동별, 업종별로 저장된 통계 데이터를 조회할 수 있는 RESTful API 제공
- **💾 공공 데이터 수집 및 저장**: 통계청, 공공데이터포털의 데이터를 수집하여 행정동 기준으로 정제 및 저장

<br>

## 3. 기술 스택

| 구분 | 기술 |
| :--- | :--- |
| **Backend** | `Java 17`, `Spring Boot 3.x`, `Spring Data JPA` |
| **Database** | `MySQL 8.0` |
| **Build Tool** | `Gradle 8.x` |

<br>

## 4. 프로젝트 구조 및 진행 방향

### 🔹 데이터 분석 및 API 설계
클라이언트에게 최종적으로 제공할 데이터 구조를 우선적으로 디자인했습니다. 이를 통해 필요한 데이터 항목을 명확히 하고, 효율적인 DB 스키마(ERD) 설계와 API 개발의 기반을 마련했습니다.

**[ 최종 API 응답 데이터 구조 예시 ]**
```json
{
    "행정동_코드": "21090710",
    "전체_주소": "부산광역시 해운대구 반여4동",
    "경계좌표": "POLYGON (( ... ))",
    "학교_목록": [
        {
            "학교명": "반안중학교",
            "학생수": 457,
            "학교급": "중"
        },
        {
            "학교명": "삼어초등학교",
            "학생수": 332,
            "학교급": "초"
        }
    ],
    "업종_통계": {
        "기업체수": "12",
        "종사자수": "36",
        "업종명": "한식"
    }
}
```

### 🔹 개발 파이프라인
1.  **데이터 수집 및 저장 (POST)**
- 통계청 및 공공데이터포털의 API를 호출하여 상권 관련 데이터를 수집합니다.
- 수집된 원본 데이터를 행정동 기준으로 정제하고 가공하여 로컬 MySQL 데이터베이스에 저장하는 API를 개발했습니다.

2.  **기초 정보 제공 API (GET)**
- 클라이언트가 업종과 지역을 선택하는 데 필요한 기초 데이터를 제공합니다.
- DB에 저장된 전체 업종 코드 목록과 행정동 정보(시도/시군구/행정동)를 조회하는 API를 개발했습니다.

3.  **최종 분석 데이터 제공 API (GET)**
- 사용자가 선택한 지역과 업종을 기반으로 파워 지수를 계산합니다.
- DB에 저장된 데이터를 종합 분석하여 위 JSON 구조와 같이 최종 결과를 반환하는 핵심 API를 개발했습니다.

<br>

## 5. 주요 문제 해결 과정

### 겪었던 문제: `DataBufferLimitException` 발생
공공데이터 API를 통해 대용량 데이터를 비동기적으로 호출하는 과정에서 `DataBufferLimitException`이 발생했습니다. 이는 Spring WebFlux의 `WebClient`가 처리할 수 있는 기본 버퍼 크기(256KB)를 초과하여 발생한 문제였습니다.

### 해결 과정
`WebClient`의 버퍼 사이즈를 직접 설정하는 `WebClientConfig`를 생성하여 문제를 해결했습니다.

1.  **`WebClientConfig.java` 설정 파일 생성**: `@Configuration` 어노테이션을 사용하여 설정 파일임을 명시했습니다.
2.  **`WebClient` Bean 재정의**: `@Bean`을 통해 `WebClient`를 직접 생성하며, `ExchangeStrategies`를 사용하여 **메모리 버퍼 크기를 10MB로 넉넉하게 상향 조정**했습니다.

    ```java
    // WebClientConfig.java

    @Configuration
    public class WebClientConfig {

        @Bean
        public WebClient webClient() {
            ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                    .build();

            return WebClient.builder()
                    .exchangeStrategies(exchangeStrategies)
                    .build();
        }
    }
    ```
이를 통해 대용량 데이터도 안정적으로 처리할 수 있는 비동기 API 호출 환경을 구축했습니다.

<br>

## 6. 느낀점 및 성장 포인트

이번 프로젝트를 통해 **요구사항 분석과 데이터 설계의 중요성**을 다시 한번 깊이 깨달았습니다. 단순히 기능 구현에 앞서, "클라이언트에게 어떤 가치를, 어떤 형태로 제공할 것인가?"를 먼저 정의하는 것이 모든 개발 과정의 나침반이 된다는 것을 배웠습니다.

최종 결과물(JSON)을 먼저 설계하니, 필요한 데이터가 무엇인지 명확해졌고 이를 바탕으로 효율적인 ERD 설계와 신중한 데이터 호출 계획을 세울 수 있었습니다. 이 경험은 향후 더 복잡한 시스템을 설계하고 개발하는 데 있어 중요한 밑거름이 될 것입니다.

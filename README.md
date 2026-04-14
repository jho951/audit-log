# audit-log

[![Build](https://github.com/jho951/audit-log/actions/workflows/build.yml/badge.svg)](https://github.com/jho951/audit-log/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jho951/audit-log-core?label=maven%20central)](https://central.sonatype.com/search?q=jho951)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](./LICENSE)
[![Tag](https://img.shields.io/github/v/tag/jho951/audit-log)](https://github.com/jho951/audit-log/tags)

## 공개 좌표

- `io.github.jho951:audit-log-core`
- `io.github.jho951:audit-log-api`

## 무엇을 제공하나

- `audit-log-core`: 기본 감사 로거, 파일/HTTP sink, fan-out/async sink, 마스킹 유틸리티
- `audit-log-api`: 감사 이벤트 모델, 로거/sink 계약, 컨텍스트/마스킹 SPI

## 책임 경계

이 저장소는 1계층 감사 로그 기능만 제공합니다.

- 감사 이벤트 표준 모델
- 행위자, 이벤트 유형, 처리 결과 모델
- 요청/추적 컨텍스트 모델
- `AuditLogger` 기록 계약
- `AuditSink` 저장소 계약
- `AuditContextResolver` SPI
- `AuditMaskingPolicy` SPI
- 기본 감사 로거 구현
- 파일/HTTP 기반 범용 sink
- 합성 sink와 비동기 sink
- 민감정보 마스킹 유틸리티

### 포함 X

- 특정 서비스 감사 정책
- 특정 조직 actor/resource 규약
- gateway/internal boundary 규칙
- 도메인 권한 모델
- 서비스별 감사 이벤트 강제 규칙
- 특정 로그 수집 제품 전용 연동
- Spring, Servlet, WebFlux 같은 framework integration

## 빠른 시작

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jho951:audit-log-core:<version>")
    implementation("io.github.jho951:audit-log-api:<version>")
}
```

## [문서](docs/README.md)

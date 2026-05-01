# audit-log

[![Build](https://github.com/jho951/audit-log/actions/workflows/build.yml/badge.svg)](https://github.com/jho951/audit-log/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jho951/audit-log-core?label=maven%20central)](https://central.sonatype.com/search?q=jho951)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](./LICENSE)
[![Tag](https://img.shields.io/github/v/tag/jho951/audit-log)](https://github.com/jho951/audit-log/tags)

## OSS Contract Source

- 기준 레포: `https://github.com/jho951/oss-contract`
- 동기화 기준 파일: [contract.lock.yml](contract.lock.yml)
- PR에서는 `.github/workflows/contract-check.yml`이 `oss-contract` lock과 1계층 공개 표면 변경 여부를 검사합니다.
- 감사 이벤트 모델, sink/context 계약, docs 구조, publish 구조 변경 시 `oss-contract`의 `registry/layer1` 기준을 먼저 확인합니다.

## 공개 좌표

- `io.github.jho951:audit-log-core`
- `io.github.jho951:audit-log-api`

## 무엇을 제공하나

- `audit-log-core`: 기본 감사 로거, 파일/HTTP sink, fan-out/async sink, 마스킹 유틸리티
- `audit-log-api`: 감사 이벤트 모델, 로거/sink 계약, 컨텍스트/마스킹 SPI

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

## 문서

- [문서](docs/README.md)

# Audit Log 문서

현재 코드 구조는 감사 로그 도메인을 위한 1계층 멀티 모듈입니다.
배포 artifactId는 `audit-log-api`, `audit-log-core`입니다.

## 문서 목록

1. [아키텍처](./ARCHITECTURE.md)
2. [구현 가이드](./implementation-guide.md)
3. [설정 가이드](./CONFIGURATION.md)
4. [사용 가이드](./USAGE.md)
5. [운영 런북](./RUNBOOK.md)
6. [트러블슈팅](./TROUBLESHOOTING.md)

## 빠른 요약

- 공개 좌표
  - `io.github.jho951:audit-log-api`
  - `io.github.jho951:audit-log-core`
- 기본 패키지
  - `com.auditlog.api`
  - `com.auditlog.spi`
  - `com.auditlog.core`
- 제공 범위: 핵심 모델, 핵심 인터페이스/계약, 기능 구현 자체

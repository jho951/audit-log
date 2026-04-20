# audit-log docs

현재 코드 구조는 감사 로그 도메인을 위한 1계층 멀티 모듈입니다.
배포 artifactId는 `audit-log-api`, `audit-log-core`입니다.

## 문서 목록

1. [architecture.md](architecture.md)
2. [modules.md](modules.md)
3. [extension-guide.md](extension-guide.md)
4. [implementation-guide.md](implementation-guide.md)
5. [test-and-ci.md](test-and-ci.md)
6. [troubleshooting.md](troubleshooting.md)

## 빠른 요약

- 공개 좌표
  - `io.github.jho951:audit-log-api`
  - `io.github.jho951:audit-log-core`
- 기본 패키지
  - `com.auditlog.api`
  - `com.auditlog.spi`
  - `com.auditlog.core`
- 제공 범위: 핵심 모델, 핵심 인터페이스/계약, 기능 구현 자체

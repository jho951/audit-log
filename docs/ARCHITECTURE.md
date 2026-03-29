# 아키텍처

## 모듈 구조

```text
audit-log/
├─ api      : 표준 이벤트 모델, 기록 API, SPI
├─ core     : logger 파이프라인과 sink 구현
└─ config   : Spring Boot starter, 웹 요청 메타데이터 수집
```

## 핵심 타입

### `api`

- `AuditEvent`: 표준 감사 이벤트
- `AuditActorType`, `AuditEventType`, `AuditResult`: 표준 분류 값
- `AuditContext`: 요청/추적 메타데이터
- `AuditLogger`: 감사 로그 기록 진입점
- `AuditSink`: 저장소 추상화

### `spi` (패키지는 `api` 모듈 내부)

- `AuditContextResolver`: 실행 컨텍스트 주입
- `AuditMaskingPolicy`: 민감정보 마스킹 정책

### `core`

- `DefaultAuditLogger`: 컨텍스트 주입 + 마스킹 + sink 위임
- `FileAuditSink`: JSON Line 파일 기록
- `ElkHttpAuditSink`: HTTP 비동기 전송
- `CompositeAuditSink`: fan-out
- `AsyncAuditSink`: executor 기반 비동기 래퍼

### `config`

- `AuditLogAutoConfiguration`: starter 자동 구성
- `AuditLogProperties`: 속성 바인딩
- `WebAuditContextFilter`: 요청 메타데이터 수집
- `WebAuditContextResolver`: 요청 컨텍스트를 이벤트에 반영

## 데이터 흐름

1. 애플리케이션이 `AuditLogger`를 호출합니다.
2. `DefaultAuditLogger`가 등록된 `AuditContextResolver`들을 순서대로 조회해 요청 메타데이터를 채웁니다.
3. `AuditMaskingPolicy`가 `details`의 민감정보를 마스킹합니다.
4. 최종 이벤트를 `AuditSink`가 파일 또는 HTTP 저장소로 기록합니다.

## 설계 특징

- 표준화: 서비스별 제각각인 감사 필드를 공통 모델로 통합
- 확장성: sink, context resolver, masking policy 교체 가능
- 웹 친화성: HTTP 요청에서 traceId/requestId/clientIp/userAgent 자동 수집
- 운영성: fail-open 유지, 비동기 옵션 제공

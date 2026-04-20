# 트러블슈팅

## 1. 파일 로그가 생성되지 않음

확인 순서:

1. `FileAuditSink`가 `AuditLogger`에 연결되어 있는지 확인
2. 파일 경로와 디렉터리 권한 확인
3. 애플리케이션 코드에서 `AuditLogger.logSuccess(...)`/`logFailure(...)`가 실제 호출되는지 확인
4. `AsyncAuditSink` 사용 시 executor가 종료되었거나 작업이 거부되지 않는지 확인

## 2. 요청 메타데이터가 비어 있음

확인 순서:

1. 애플리케이션에서 `AuditContextResolver`를 직접 구현했는지 확인
2. `DefaultAuditLogger` 생성 시 resolver 목록에 포함했는지 확인
3. 요청 헤더(`X-Trace-Id`, `X-Request-Id`, `X-Forwarded-For`, `traceparent`) 매핑 로직을 확인

## 3. HTTP 전송이 누락됨

확인 순서:

1. `HttpAuditSink`가 `AuditLogger` 또는 `CompositeAuditSink`에 연결되어 있는지 확인
2. endpoint 값 확인
3. 인증 키 유효성 확인
4. 네트워크 egress / 방화벽 정책 확인

## 4. 민감정보가 마스킹되지 않음

확인 순서:

1. `details` 키 이름이 민감 키 목록과 매칭되는지 확인
2. 중첩 구조가 `Map`, `Iterable`, 배열 형태인지 확인
3. `AuditMaskingPolicy`가 `DefaultAuditLogger`에 전달되었는지 확인

## 5. 모듈/패키지 이름 혼선

확인 순서:

1. Gradle 모듈이 `:audit-log-api`, `:audit-log-core`인지 확인
2. 의존성 좌표가 `io.github.jho951:audit-log-api`, `io.github.jho951:audit-log-core`인지 확인
3. 패키지는 `com.auditlog.api`, `com.auditlog.spi`, `com.auditlog.core`를 사용하는지 확인

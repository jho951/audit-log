# 구성

`audit-log`는 Java 코드에서 직접 조립하는 프레임워크-중립 모듈입니다.

## 기본 구성 요소

- `AuditLogger`: 감사 이벤트 기록 진입점
- `AuditSink`: 실제 저장소로 쓰는 대상
- `AuditContextResolver`: trace/request/client 정보를 채우는 SPI
- `AuditMaskingPolicy`: `details` 마스킹 정책

## 권장 구성 방식

```java
AuditSink sink = new CompositeAuditSink(List.of(
    new FileAuditSink(Path.of("logs/audit.log"), "sample-service", "local"),
    new HttpAuditSink(URI.create("https://audit.example.com/events"), "sample-service", "local", authorization)
));

AuditMaskingPolicy maskingPolicy = details -> AuditMasker.mask(details, Set.of("password", "token", "secret"));
AuditLogger logger = new DefaultAuditLogger(sink, List.of(new CustomAuditContextResolver()), maskingPolicy);
```

## 동작 규칙

- `AuditContextResolver`는 여러 개를 등록할 수 있습니다.
- 기존 이벤트에 값이 있으면 컨텍스트 해석 결과로 덮어쓰지 않습니다.
- `AuditMaskingPolicy`가 없으면 마스킹을 적용하지 않습니다.
- `AsyncAuditSink`를 감싸면 sink 호출을 비동기로 처리할 수 있습니다.

## 민감정보 마스킹

`details`의 `password`, `token`, `secret` 같은 키는 기본 정책에서 마스킹할 수 있습니다.

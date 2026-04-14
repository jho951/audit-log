# 사용 가이드

## 의존성

```gradle
dependencies {
    implementation("io.github.jho951:audit-log-api:<version>")
    implementation("io.github.jho951:audit-log-core:<version>")
}
```

## 기본 사용

```java
AuditLogger auditLogger = new DefaultAuditLogger(
    new FileAuditSink(Path.of("logs/audit.log"), "sample-service", "local"),
    List.of(),
    details -> details
);

auditLogger.logSuccess(
    AuditEvent.builder(AuditEventType.CREATE, "CREATE_DOCUMENT")
        .actor(userId, AuditActorType.USER, null)
        .resource("DOCUMENT", documentId)
);
```

## 핵심 포인트

- 서비스에서 감사 이벤트를 직접 생성합니다.
- 요청 메타데이터가 필요하면 `AuditContextResolver`를 직접 구현해 주입합니다.
- 민감정보는 `AuditMaskingPolicy`로 제어합니다.

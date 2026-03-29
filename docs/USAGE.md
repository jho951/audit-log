# 사용 가이드

## 기본 사용

```java
import com.auditlog.api.AuditActorType;
import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditEventType;
import com.auditlog.api.AuditLogger;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    private final AuditLogger auditLogger;

    public DocumentService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    public void create(String userId, String documentId) {
        auditLogger.logSuccess(
            AuditEvent.builder(AuditEventType.CREATE, "CREATE_DOCUMENT")
                .actor(userId, AuditActorType.USER, null)
                .resource("DOCUMENT", documentId)
                .detail("source", "api")
        );
    }
}
```

## 실패 이벤트 기록

```java
auditLogger.logFailure(
    AuditEvent.builder(AuditEventType.DELETE, "DELETE_DOCUMENT")
        .actor("admin-1", AuditActorType.ADMIN, "root")
        .resource("DOCUMENT", "doc-123")
        .detail("reasonCode", "NO_PERMISSION"),
    "ACCESS_DENIED"
);
```

## 요청 메타데이터 자동 수집

웹 환경에서는 다음 값이 자동 주입됩니다.

- `traceId`
- `requestId`
- `clientIp`
- `userAgent`

서비스 코드는 이 필드를 직접 채우지 않아도 됩니다.

## 민감정보 마스킹

`details`에 다음과 같은 키가 포함되면 값이 `****`로 마스킹됩니다.

- `password`
- `token`
- `authorization`
- `email`

중첩된 맵/리스트 구조도 마스킹됩니다.


## 지원 헤더

웹 환경에서는 아래 헤더를 우선순위에 따라 사용합니다.

- traceId: `X-Trace-Id` → `X-B3-TraceId` → `traceparent`
- requestId: `X-Request-Id` → `X-Correlation-Id`
- clientIp: `X-Forwarded-For` → `X-Real-IP` → `remoteAddr`

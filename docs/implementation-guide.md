# 구현 가이드

## 기본 파일 sink 조립

```java
import com.auditlog.api.AuditActorType;
import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditEventType;
import com.auditlog.api.AuditLogger;
import com.auditlog.api.AuditSink;
import com.auditlog.core.DefaultAuditLogger;
import com.auditlog.core.FileAuditSink;

import java.nio.file.Path;
import java.util.List;

AuditSink sink = new FileAuditSink(
    Path.of("logs/audit.log"),
    "checkout-service",
    "local"
);

AuditLogger auditLogger = new DefaultAuditLogger(
    sink,
    List.of(),
    details -> details
);

auditLogger.logSuccess(
    AuditEvent.builder(AuditEventType.CREATE, "CREATE_ORDER")
        .actor("user-1", AuditActorType.USER, null)
        .resource("ORDER", "order-1")
        .detail("amount", 12000)
);
```

## 컨텍스트와 마스킹 조립

컨텍스트와 마스킹을 함께 조립할 때:

```java
import com.auditlog.api.AuditContext;
import com.auditlog.api.AuditLogger;
import com.auditlog.core.AuditMasker;
import com.auditlog.core.CompositeAuditSink;
import com.auditlog.core.DefaultAuditLogger;
import com.auditlog.core.FileAuditSink;
import com.auditlog.core.HttpAuditSink;
import com.auditlog.spi.AuditContextResolver;
import com.auditlog.spi.AuditMaskingPolicy;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

AuditContextResolver contextResolver = event ->
    new AuditContext("trace-1", "request-1", "127.0.0.1", "JUnit");

AuditMaskingPolicy maskingPolicy = details ->
    AuditMasker.mask(details, Set.of("password", "token", "secret"));

AuditLogger auditLogger = new DefaultAuditLogger(
    new CompositeAuditSink(List.of(
        new FileAuditSink(Path.of("logs/audit.log"), "checkout-service", "local"),
        new HttpAuditSink(URI.create("https://audit.example.com/events"), "checkout-service", "local", "Bearer token")
    )),
    List.of(contextResolver),
    maskingPolicy
);
```

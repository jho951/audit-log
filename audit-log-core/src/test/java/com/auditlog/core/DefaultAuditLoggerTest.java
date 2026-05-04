package com.auditlog.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.auditlog.api.AuditSink;
import com.auditlog.api.exception.AuditException;
import com.auditlog.api.exception.AuditFailureReason;
import com.auditlog.api.model.AuditContext;
import com.auditlog.api.model.AuditEvent;
import com.auditlog.api.model.AuditEventType;
import com.auditlog.api.model.AuditResult;
import com.auditlog.spi.AuditContextResolver;
import com.auditlog.spi.AuditMaskingPolicy;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class DefaultAuditLoggerTest {

	@Test
	void loggerShouldApplyContextAndMaskingBeforeWrite() {
		CapturingAuditSink sink = new CapturingAuditSink();
		AuditContextResolver resolver = event -> new AuditContext("trace-1", "req-1", "127.0.0.1", "JUnit");
		AuditMaskingPolicy maskingPolicy = details -> AuditMasker.mask(details, java.util.Set.of("password"));
		DefaultAuditLogger logger = new DefaultAuditLogger(sink, List.of(resolver), maskingPolicy);

		logger.log(AuditEvent.builder(AuditEventType.LOGIN, "USER_LOGIN")
			.detail("password", "plain-text")
			.build());

		assertNotNull(sink.event);
		assertEquals("trace-1", sink.event.getTraceId());
		assertEquals("req-1", sink.event.getRequestId());
		assertEquals("127.0.0.1", sink.event.getClientIp());
		assertEquals("JUnit", sink.event.getUserAgent());
		assertEquals("****", sink.event.getDetails().get("password"));
	}

	@Test
	void loggerShouldSupportFailureConvenienceMethod() {
		CapturingAuditSink sink = new CapturingAuditSink();
		DefaultAuditLogger logger = new DefaultAuditLogger(sink, List.of(), Map::copyOf);

		logger.logFailure(
			AuditEvent.builder(AuditEventType.DELETE, "DELETE_ACCOUNT").detail("target", "user-1"),
			"ACCESS_DENIED"
		);

		assertEquals(AuditResult.FAILURE, sink.event.getResult());
		assertEquals("ACCESS_DENIED", sink.event.getReason());
	}

	@Test
	void loggerShouldRejectNullSink() {
		AuditException exception = assertThrows(AuditException.class, () ->
			new DefaultAuditLogger(null, List.of(), null)
		);

		assertEquals(AuditFailureReason.INVALID_CONFIGURATION, exception.getReason());
	}

	@Test
	void loggerShouldRejectNullEvent() {
		CapturingAuditSink sink = new CapturingAuditSink();
		DefaultAuditLogger logger = new DefaultAuditLogger(sink, List.of(), null);

		AuditException exception = assertThrows(AuditException.class, () -> logger.log(null));

		assertEquals(AuditFailureReason.INVALID_EVENT, exception.getReason());
	}

	private static final class CapturingAuditSink implements AuditSink {
		private AuditEvent event;

		@Override
		public void write(AuditEvent event) {
			this.event = event;
		}
	}
}

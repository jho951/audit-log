package com.auditlog.core.sink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import com.auditlog.api.AuditSink;
import com.auditlog.api.exception.AuditException;
import com.auditlog.api.exception.AuditFailureReason;

import org.junit.jupiter.api.Test;

class AuditSinkValidationTest {

	@Test
	void fileAuditSinkShouldRejectNullPath() {
		AuditException exception = assertThrows(AuditException.class, () ->
			new FileAuditSink(null, "svc", "test")
		);

		assertEquals(AuditFailureReason.INVALID_CONFIGURATION, exception.getReason());
	}

	@Test
	void httpAuditSinkShouldRejectNullEndpoint() {
		AuditException exception = assertThrows(AuditException.class, () ->
			new HttpAuditSink(null, "svc", "test", null)
		);

		assertEquals(AuditFailureReason.INVALID_CONFIGURATION, exception.getReason());
	}

	@Test
	void asyncAuditSinkShouldRejectNullExecutor() {
		AuditSink delegate = event -> {};
		AuditException exception = assertThrows(AuditException.class, () ->
			new AsyncAuditSink(delegate, null)
		);

		assertEquals(AuditFailureReason.INVALID_CONFIGURATION, exception.getReason());
	}

	@Test
	void compositeAuditSinkShouldRejectNullEntries() {
		AuditSink delegate = event -> {};
		List<AuditSink> sinks = new ArrayList<>();
		sinks.add(delegate);
		sinks.add(null);

		AuditException exception = assertThrows(AuditException.class, () ->
			new CompositeAuditSink(sinks)
		);

		assertEquals(AuditFailureReason.INVALID_CONFIGURATION, exception.getReason());
	}
}

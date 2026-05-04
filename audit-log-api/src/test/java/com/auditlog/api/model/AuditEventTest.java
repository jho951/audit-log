package com.auditlog.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import com.auditlog.api.exception.AuditException;
import com.auditlog.api.exception.AuditFailureReason;

import org.junit.jupiter.api.Test;

class AuditEventTest {

	@Test
	void builderShouldPopulateDefaultAuditFields() {
		AuditEvent event = AuditEvent.builder(AuditEventType.CREATE, "CREATE_DOCUMENT")
			.actor("user-1", AuditActorType.USER, "alice")
			.resource("DOCUMENT", "doc-1")
			.detail("changedFields", 2)
			.build();

		assertNotNull(event.getEventId());
		assertNotNull(event.getOccurredAt());
		assertEquals(AuditResult.SUCCESS, event.getResult());
		assertEquals(AuditActorType.USER, event.getActorType());
		assertEquals(2, event.getDetails().get("changedFields"));
	}

	@Test
	void toBuilderShouldPreserveAndExtendExistingEvent() {
		AuditEvent original = AuditEvent.builder(AuditEventType.UPDATE, "UPDATE_PERMISSION")
			.actor("admin-1", AuditActorType.ADMIN, "root")
			.resource("USER", "user-99")
			.failure("FORBIDDEN")
			.details(Map.of("email", "user@example.com"))
			.build();

		AuditEvent copied = original.toBuilder()
			.requestId("req-1")
			.build();

		assertEquals(original.getEventId(), copied.getEventId());
		assertEquals("FORBIDDEN", copied.getReason());
		assertEquals("req-1", copied.getRequestId());
		assertFalse(copied.getDetails().isEmpty());
	}

	@Test
	void builderShouldRejectBlankAction() {
		AuditException exception = assertThrows(AuditException.class, () ->
			AuditEvent.builder(AuditEventType.CREATE, "  ").build()
		);

		assertEquals(AuditFailureReason.INVALID_EVENT, exception.getReason());
	}
}

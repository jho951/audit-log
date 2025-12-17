package com.auditlog.api;

import java.util.Map;

public interface AuditLogger {
	void log(AuditEvent event);

	default void log(
		AuditEventType eventType,
		String action,
		String actorId,
		String actorIp,
		String resourceType,
		String resourceId,
		String result,
		String traceId,
		Map<String, String> details
	) {
		AuditEvent.Builder b = AuditEvent.builder(eventType, action)
			.actorId(actorId)
			.actorIp(actorIp)
			.resource(resourceType, resourceId)
			.result(result)
			.traceId(traceId);

		if (details != null) {
			details.forEach(b::detail);
		}

		log(b.build());
	}
}

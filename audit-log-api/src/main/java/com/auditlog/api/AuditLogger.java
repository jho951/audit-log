package com.auditlog.api;

import java.util.Map;

import com.auditlog.api.model.AuditActorType;
import com.auditlog.api.model.AuditEvent;
import com.auditlog.api.model.AuditEventType;
import com.auditlog.api.model.AuditResult;

/** 감사 이벤트를 기록하는 진입점 인터페이스입니다. */
public  interface AuditLogger {
	/** 로그 기록 (The Logger) */
	void log(AuditEvent event);
	/** 성공 로그 */
	default void logSuccess(AuditEvent.Builder builder) {
		log(builder.success().build());
	}
	/** 성공 로그 */
	default void logFailure(AuditEvent.Builder builder, String reason) {
		log(builder.failure(reason).build());
	}

	default void log(
		AuditEventType eventType,
		String action,
		String actorId,
		AuditActorType actorType,
		String actorName,
		String resourceType,
		String resourceId,
		AuditResult result,
		String reason,
		Map<String, ?> details
	) {
		AuditEvent.Builder builder = AuditEvent.builder(eventType, action)
			.actor(actorId, actorType, actorName)
			.resource(resourceType, resourceId)
			.result(result)
			.reason(reason)
			.details(details);
		log(builder.build());
	}
}

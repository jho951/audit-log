package com.auditlog.core;

import java.util.List;
import java.util.Objects;

import com.auditlog.api.AuditLogger;
import com.auditlog.api.AuditSink;
import com.auditlog.api.exception.AuditException;
import com.auditlog.api.exception.AuditFailureReason;
import com.auditlog.api.model.AuditContext;
import com.auditlog.api.model.AuditEvent;
import com.auditlog.spi.AuditContextResolver;
import com.auditlog.spi.AuditMaskingPolicy;

/**
 * {@link AuditLogger}의 기본 구현체입니다.
 * 이벤트에 컨텍스트와 마스킹 정책을 적용한 뒤 sink로 전달합니다.
 */
public final class DefaultAuditLogger implements AuditLogger {
	private final AuditSink sink;
	private final List<AuditContextResolver> contextResolvers;
	private final AuditMaskingPolicy maskingPolicy;

	public DefaultAuditLogger(
		AuditSink sink,
		List<AuditContextResolver> contextResolvers,
		AuditMaskingPolicy maskingPolicy
	) {
		this.sink = requireConfiguration(sink, "Audit sink must not be null.");
		this.contextResolvers = copyResolvers(contextResolvers);
		this.maskingPolicy = maskingPolicy;
	}

	@Override
	public void log(AuditEvent event) {
		if (event == null) {
			throw new AuditException(AuditFailureReason.INVALID_EVENT, "Audit event must not be null.");
		}
		AuditEvent enriched = applyContext(event);
		AuditEvent masked = applyMasking(enriched);
		sink.write(masked);
	}

	private static List<AuditContextResolver> copyResolvers(List<AuditContextResolver> contextResolvers) {
		if (contextResolvers == null) return List.of();
		if (contextResolvers.stream().anyMatch(Objects::isNull)) {
			throw new AuditException(AuditFailureReason.INVALID_CONFIGURATION, "Audit context resolvers must not contain null.");
		}
		return List.copyOf(contextResolvers);
	}

	private static <T> T requireConfiguration(T value, String message) {
		if (value == null) {
			throw new AuditException(AuditFailureReason.INVALID_CONFIGURATION, message);
		}
		return value;
	}

	private AuditEvent applyContext(AuditEvent event) {
		String traceId = event.getTraceId();
		String requestId = event.getRequestId();
		String clientIp = event.getClientIp();
		String userAgent = event.getUserAgent();

		for (AuditContextResolver resolver : contextResolvers) {
			AuditContext context = resolver.resolve(event);
			if (context == null) continue;
			if (isBlank(traceId) && !isBlank(context.getTraceId())) traceId = context.getTraceId();
			if (isBlank(requestId) && !isBlank(context.getRequestId())) requestId = context.getRequestId();
			if (isBlank(clientIp) && !isBlank(context.getClientIp())) clientIp = context.getClientIp();
			if (isBlank(userAgent) && !isBlank(context.getUserAgent())) userAgent = context.getUserAgent();
		}

		if (same(event.getTraceId(), traceId)
			&& same(event.getRequestId(), requestId)
			&& same(event.getClientIp(), clientIp)
			&& same(event.getUserAgent(), userAgent)) {
			return event;
		}

		return event.toBuilder()
			.traceId(traceId)
			.requestId(requestId)
			.clientIp(clientIp)
			.userAgent(userAgent)
			.build();
	}

	private AuditEvent applyMasking(AuditEvent event) {
		if (maskingPolicy == null) return event;
		return event.toBuilder()
			.details(maskingPolicy.mask(event.getDetails()))
			.build();
	}

	private static boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	private static boolean same(String left, String right) {
		return Objects.equals(left, right);
	}
}

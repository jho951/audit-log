package com.auditlog.core.sink;

import java.util.List;
import java.util.Objects;

import com.auditlog.api.AuditSink;
import com.auditlog.api.exception.AuditException;
import com.auditlog.api.exception.AuditFailureReason;
import com.auditlog.api.model.AuditEvent;

/** 여러 {@link AuditSink}로 이벤트를 fan-out 전송하는 합성 sink입니다. */
public final class CompositeAuditSink implements AuditSink {
	private final List<AuditSink> sinks;

	public CompositeAuditSink(List<AuditSink> sinks) {
		this.sinks = copySinks(sinks);
	}

	@Override
	public void write(AuditEvent event) {
		for (AuditSink sink : sinks) {
			try {
				sink.write(event);
			} catch (Exception ignored) {
			}
		}
	}

	@Override
	public void close() {
		for (AuditSink sink : sinks) {
			try {
				sink.close();
			} catch (Exception ignored) {
			}
		}
	}

	private static List<AuditSink> copySinks(List<AuditSink> sinks) {
		if (sinks == null) {
			throw new AuditException(AuditFailureReason.INVALID_CONFIGURATION, "Composite audit sinks must not be null.");
		}
		if (sinks.stream().anyMatch(Objects::isNull)) {
			throw new AuditException(AuditFailureReason.INVALID_CONFIGURATION, "Composite audit sinks must not contain null.");
		}
		return List.copyOf(sinks);
	}
}

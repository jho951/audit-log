package com.auditlog.core;

import java.util.List;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

/** 여러 {@link AuditSink}로 이벤트를 fan-out 전송하는 합성 sink입니다. */
public final class CompositeAuditSink implements AuditSink {
	private final List<AuditSink> sinks;

	public CompositeAuditSink(List<AuditSink> sinks) {
		this.sinks = List.copyOf(sinks);
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
}

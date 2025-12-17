package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

import java.util.List;

public final class CompositeAuditSink implements AuditSink {
	private final List<AuditSink> sinks;

	public CompositeAuditSink(List<AuditSink> sinks) {
		this.sinks = List.copyOf(sinks);
	}

	@Override
	public void append(AuditEvent event) {
		for (AuditSink s : sinks) {
			try { s.append(event); } catch (Exception ignored) {}
		}
	}

	@Override
	public void close() {
		for (AuditSink s : sinks) {
			try { s.close(); } catch (Exception ignored) {}
		}
	}
}

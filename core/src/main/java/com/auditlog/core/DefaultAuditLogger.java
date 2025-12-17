package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditLogger;
import com.auditlog.api.AuditSink;

public final class DefaultAuditLogger implements AuditLogger {
	private final AuditSink sink;

	public DefaultAuditLogger(AuditSink sink) {
		this.sink = sink;
	}

	@Override
	public void log(AuditEvent event) {
		sink.append(event);
	}
}

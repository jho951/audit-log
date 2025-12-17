package com.auditlog.api;

public interface AuditSink extends AutoCloseable {
	void append(AuditEvent event);

	@Override
	default void close() { /* no-op */ }
}

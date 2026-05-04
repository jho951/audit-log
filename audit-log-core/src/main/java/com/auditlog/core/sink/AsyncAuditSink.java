package com.auditlog.core.sink;

import com.auditlog.api.AuditSink;
import com.auditlog.api.exception.AuditException;
import com.auditlog.api.exception.AuditFailureReason;
import com.auditlog.api.model.AuditEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/** sink 기록을 별도 executor에서 비동기로 처리합니다. */
public final class AsyncAuditSink implements AuditSink {
	private final AuditSink delegate;
	private final ExecutorService executorService;

	public AsyncAuditSink(AuditSink delegate, ExecutorService executorService) {
		this.delegate = requireConfiguration(delegate, "Async audit delegate sink must not be null.");
		this.executorService = requireConfiguration(executorService, "Async audit executor service must not be null.");
	}

	@Override
	public void write(AuditEvent event) {
		try {
			executorService.execute(() -> delegate.write(event));
		} catch (RuntimeException ignored) {
			// fail-open
		}
	}

	@Override
	public void close() {
		try {
			executorService.shutdown();
			executorService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException interruptedException) {
			Thread.currentThread().interrupt();
		} finally {
			delegate.close();
		}
	}

	private static <T> T requireConfiguration(T value, String message) {
		if (value == null) throw new AuditException(AuditFailureReason.INVALID_CONFIGURATION, message);
		return value;
	}
}

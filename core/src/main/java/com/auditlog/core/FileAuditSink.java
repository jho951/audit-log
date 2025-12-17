package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public final class FileAuditSink implements AuditSink {
	private final Path filePath;
	private final String serviceName;
	private final String env;
	private final ReentrantLock lock = new ReentrantLock();

	public FileAuditSink(Path filePath, String serviceName, String env) {
		this.filePath = Objects.requireNonNull(filePath);
		this.serviceName = serviceName == null ? "unknown-service" : serviceName;
		this.env = env == null ? "local" : env;
		ensureParent();
	}

	@Override
	public void append(AuditEvent event) {
		String line = Json.toJsonLine(event, serviceName, env);
		lock.lock();
		try {
			Files.writeString(
				filePath,
				line,
				StandardOpenOption.CREATE,
				StandardOpenOption.WRITE,
				StandardOpenOption.APPEND
			);
		} catch (IOException ex) {
			// 감사 로그는 "업무 실패"로 번지지 않게 기본은 삼킴(원하면 전략화 가능)
		} finally {
			lock.unlock();
		}
	}

	private void ensureParent() {
		try {
			Path parent = filePath.getParent();
			if (parent != null) Files.createDirectories(parent);
		} catch (IOException ignored) {}
	}
}

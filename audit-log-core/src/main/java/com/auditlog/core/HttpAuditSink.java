package com.auditlog.core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

/** 감사 이벤트를 HTTP endpoint로 전송하는 sink 구현체입니다. */
public final class HttpAuditSink implements AuditSink {
	private final HttpClient client;
	private final URI endpoint;
	private final String serviceName;
	private final String env;
	private final String authorization;

	public HttpAuditSink(URI endpoint, String serviceName, String env, String authorization) {
		this.client = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(2))
			.build();
		this.endpoint = endpoint;
		this.serviceName = serviceName == null ? "unknown-service" : serviceName;
		this.env = env == null ? "local" : env;
		this.authorization = authorization;
	}

	@Override
	public void write(AuditEvent event) {
		try {
			String body = Json.toJsonLine(event, serviceName, env);
			HttpRequest.Builder builder = HttpRequest.newBuilder(endpoint)
				.timeout(Duration.ofSeconds(2))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body));

			if (authorization != null && !authorization.isBlank()) {
				builder.header("Authorization", authorization);
			}

			client.sendAsync(builder.build(), HttpResponse.BodyHandlers.discarding());
		} catch (Exception ignored) {
			// fail-open
		}
	}
}

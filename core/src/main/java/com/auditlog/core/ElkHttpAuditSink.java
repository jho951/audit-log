package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class ElkHttpAuditSink implements AuditSink {
	private final HttpClient client;
	private final URI endpoint;
	private final String serviceName;
	private final String env;
	private final String apiKey; // optional

	public ElkHttpAuditSink(URI endpoint, String serviceName, String env, String apiKey) {
		this.client = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(2))
			.build();
		this.endpoint = endpoint;
		this.serviceName = serviceName == null ? "unknown-service" : serviceName;
		this.env = env == null ? "local" : env;
		this.apiKey = apiKey; // Elastic API Key 쓰면 "ApiKey xxx"
	}

	@Override
	public void append(AuditEvent event) {
		try {
			String body = Json.toJsonLine(event, serviceName, env); // 1줄 JSON
			HttpRequest.Builder b = HttpRequest.newBuilder(endpoint)
				.timeout(Duration.ofSeconds(2))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body));

			if (apiKey != null && !apiKey.isBlank()) {
				b.header("Authorization", apiKey.startsWith("ApiKey ") ? apiKey : ("ApiKey " + apiKey));
			}

			client.sendAsync(b.build(), HttpResponse.BodyHandlers.discarding());
		} catch (Exception ignored) {
			// v1: fail-open
		}
	}
}

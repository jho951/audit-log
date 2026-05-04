package com.auditlog.core.sink;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.auditlog.api.AuditSink;
import com.auditlog.api.exception.AuditException;
import com.auditlog.api.exception.AuditFailureReason;
import com.auditlog.api.model.AuditEvent;

/** 생성된 감사 로그를 HTTP 통신을 통해 외부 서버(로그 수집 서버 등)로 전송 */
public final class HttpAuditSink implements AuditSink {
	/** 데이터를 보내기 위한 자바 표준 도구 */
	private final HttpClient client;
	/** 로그를 받아줄 서버의 주소 */
	private final URI endpoint;
	/** 로그를 보낼 때 "어떤 서비스(예: 결제시스템)"에서 발생한 건지 꼬리표 */
	private final String serviceName;
	/** 로그를 보낼 때 "어떤 환경(예: 실운영, 테스트)"에서 발생한 건지 꼬리표 */
	private final String env;
	/** 보안을 위해 서버에 접속할 때 필요한 인증 키(Token) */
	private final String authorization;

	public HttpAuditSink(URI endpoint, String serviceName, String env, String authorization) {
		this.client = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(2))
			.build();
		this.endpoint = requireEndpoint(endpoint);
		this.serviceName = serviceName == null ? "unknown-service" : serviceName;
		this.env = env == null ? "local" : env;
		this.authorization = authorization;
	}

	private static URI requireEndpoint(URI endpoint) {
		if (endpoint == null) {
			throw new AuditException(AuditFailureReason.INVALID_CONFIGURATION, "Audit HTTP endpoint must not be null.");
		}
		return endpoint;
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

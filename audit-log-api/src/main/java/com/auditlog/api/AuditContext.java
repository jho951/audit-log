package com.auditlog.api;

import java.util.Objects;

/** 로깅 시점에 자동으로 주입할 요청/추적 메타데이터입니다. */
public final class AuditContext {
	/** 전체 흐름 추적 ID */
	private final String traceId;
	/** 단일 요청 식별 ID */
	private final String requestId;
	/** 사용자 접속 IP */
	private final String clientIp;
	/** 접속 환경 정보 */
	private final String userAgent;

	public AuditContext(String traceId, String requestId, String clientIp, String userAgent) {
		this.traceId = traceId;
		this.requestId = requestId;
		this.clientIp = clientIp;
		this.userAgent = userAgent;
	}

	public static final AuditContext EMPTY = new AuditContext(null, null, null, null);

	public static AuditContext empty() {return EMPTY;}
	public String getTraceId() {return traceId;}
	public String getRequestId() {return requestId;}
	public String getClientIp() {return clientIp;}
	public String getUserAgent() {return userAgent;}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AuditContext that = (AuditContext) o;
		return Objects.equals(traceId, that.traceId) &&
			Objects.equals(requestId, that.requestId) &&
			Objects.equals(clientIp, that.clientIp) &&
			Objects.equals(userAgent, that.userAgent);
	}

	@Override
	public int hashCode() {
		return Objects.hash(traceId, requestId, clientIp, userAgent);
	}

	@Override
	public String toString() {
		return "AuditContext[" +
			"traceId=" + traceId +
			", requestId=" + requestId +
			", clientIp=" + clientIp +
			", userAgent=" + userAgent +
			']';
	}
}
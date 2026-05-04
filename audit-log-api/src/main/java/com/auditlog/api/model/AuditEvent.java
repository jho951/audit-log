package com.auditlog.api.model;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.auditlog.api.exception.AuditException;
import com.auditlog.api.exception.AuditFailureReason;

/** 단일 감사 이벤트를 표현하는 불변 객체입니다. */
public final class AuditEvent {
	/** 로그에 부여되는 고유 식별 번호 (UUID) */
	private final String eventId;
	/** 이벤트가 발생한 정확한 시각 */
	private final Instant occurredAt;
	/** 이벤트 주체 아이디 */
	private final String actorId;
	/** 행위자의 성격 */
	private final AuditActorType actorType;
	/** 로그를 사람이 읽기 편하게 기록하는 이름 */
	private final String actorName;
	/** 로그의 대분류 (예: AUTH(인증), DATA(데이터 변경)) */
	private final AuditEventType eventType;
	/** 이벤트 (예: LOGIN, DELETE, UPDATE) */
	private final String action;
	/** 건드린 대상의 종류 (예: ORDER(주문), POST(게시글)) */
	private final String resourceType;
	/** 건드린 대상의 고유 번호 (예: order-20260502-001) */
	private final String resourceId;
	/** 작업의 성공/실패 여부 (SUCCESS, FAILURE) */
	private final AuditResult result;
	/** 실패 시 왜 실패했는지에 대한 이유 */
	private final String reason;
	/** 여러 서버를 거쳐가는 전체 요청의 추적 ID */
	private final String traceId;
	/** 단일 HTTP 요청의 ID */
	private final String requestId;
	/** 요청을 보낸 사용자의 IP 주소 */
	private final String clientIp;
	/** 접속할 때 사용한 브라우저/기기 정보 */
	private final String userAgent;
	/** 표준 필드 외에 추가로 남기고 싶은 데이터 */
	private final Map<String, Object> details;

	private AuditEvent(Builder builder) {
		this.eventId = builder.eventId != null ? builder.eventId : UUID.randomUUID().toString();
		this.occurredAt = builder.occurredAt != null ? builder.occurredAt : Instant.now();
		this.actorId = builder.actorId;
		this.actorType = builder.actorType != null ? builder.actorType : AuditActorType.UNKNOWN;
		this.actorName = builder.actorName;
		this.eventType = builder.eventType != null ? builder.eventType : AuditEventType.CUSTOM;
		this.action = builder.action;
		this.resourceType = builder.resourceType;
		this.resourceId = builder.resourceId;
		this.result = builder.result != null ? builder.result : AuditResult.SUCCESS;
		this.reason = builder.reason;
		this.traceId = builder.traceId;
		this.requestId = builder.requestId;
		this.clientIp = builder.clientIp;
		this.userAgent = builder.userAgent;
		this.details = Collections.unmodifiableMap(new LinkedHashMap<>(builder.details));
	}

	public String getEventId() {return eventId;}
	public Instant getOccurredAt() {return occurredAt;}
	public String getActorId() {return actorId;}
	public AuditActorType getActorType() {return actorType;}
	public String getActorName() {return actorName;}
	public AuditEventType getEventType() {return eventType;}
	public String getAction() {return action;}
	public String getResourceType() {return resourceType;}
	public String getResourceId() {return resourceId;}
	public AuditResult getResult() {return result;}
	public String getReason() {return reason;}
	public String getTraceId() {return traceId;}
	public String getRequestId() {return requestId;}
	public String getClientIp() {return clientIp;}
	public String getUserAgent() {return userAgent;}
	public Map<String, Object> getDetails() {return details;}

	public Builder toBuilder() {
		return new Builder(eventType, action)
			.eventId(eventId)
			.occurredAt(occurredAt)
			.actorId(actorId)
			.actorType(actorType)
			.actorName(actorName)
			.resource(resourceType, resourceId)
			.result(result)
			.reason(reason)
			.traceId(traceId)
			.requestId(requestId)
			.clientIp(clientIp)
			.userAgent(userAgent)
			.details(details);
	}

	public static Builder builder(AuditEventType eventType, String action) {
		return new Builder(eventType, action);
	}

	public static final class Builder {
		private String eventId;
		private Instant occurredAt;
		private String actorId;
		private AuditActorType actorType;
		private String actorName;
		private final AuditEventType eventType;
		private final String action;
		private String resourceType;
		private String resourceId;
		private AuditResult result;
		private String reason;
		private String traceId;
		private String requestId;
		private String clientIp;
		private String userAgent;
		private final Map<String, Object> details = new LinkedHashMap<>();

		public Builder(AuditEventType eventType, String action) {
			this.eventType = eventType;
			this.action = action;
		}

		public Builder eventId(String eventId) {
			this.eventId = eventId;
			return this;
		}

		public Builder occurredAt(Instant occurredAt) {
			this.occurredAt = occurredAt;
			return this;
		}

		public Builder actorId(String actorId) {
			this.actorId = actorId;
			return this;
		}

		public Builder actorType(AuditActorType actorType) {
			this.actorType = actorType;
			return this;
		}

		public Builder actorName(String actorName) {
			this.actorName = actorName;
			return this;
		}

		public Builder actor(String actorId, AuditActorType actorType, String actorName) {
			this.actorId = actorId;
			this.actorType = actorType;
			this.actorName = actorName;
			return this;
		}

		public Builder resource(String resourceType, String resourceId) {
			this.resourceType = resourceType;
			this.resourceId = resourceId;
			return this;
		}

		public Builder result(AuditResult result) {
			this.result = result;
			return this;
		}

		public Builder success() {
			this.result = AuditResult.SUCCESS;
			this.reason = null;
			return this;
		}

		public Builder failure(String reason) {
			this.result = AuditResult.FAILURE;
			this.reason = reason;
			return this;
		}

		public Builder reason(String reason) {
			this.reason = reason;
			return this;
		}

		public Builder traceId(String traceId) {
			this.traceId = traceId;
			return this;
		}

		public Builder requestId(String requestId) {
			this.requestId = requestId;
			return this;
		}

		public Builder clientIp(String clientIp) {
			this.clientIp = clientIp;
			return this;
		}

		public Builder userAgent(String userAgent) {
			this.userAgent = userAgent;
			return this;
		}

		public Builder detail(String key, Object value) {
			if (key != null && value != null) {
				this.details.put(key, value);
			}
			return this;
		}

		public Builder details(Map<String, ?> details) {
			if (details != null) {
				details.forEach((key, value) -> {
					if (key != null && value != null) {
						this.details.put(key, value);
					}
				});
			}
			return this;
		}

		public AuditEvent build() {
			if (action == null || action.isBlank()) {
				throw new AuditException(AuditFailureReason.INVALID_EVENT, "Audit action must not be blank.");
			}
			return new AuditEvent(this);
		}
	}
}

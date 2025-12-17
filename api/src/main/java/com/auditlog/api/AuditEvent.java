package com.auditlog.api;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 단일 Audit 이벤트를 표현하는 불변 객체.
 */
public final class AuditEvent {

	private final Instant timestamp;
	private final String actorId;
	private final String actorIp;
	private final AuditEventType eventType;
	private final String action;
	private final String resourceType;
	private final String resourceId;
	private final String result;
	private final Map<String, String> details;
	private final String traceId;


	private AuditEvent(Builder builder) {
		this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
		this.actorId = builder.actorId;
		this.actorIp = builder.actorIp;
		this.eventType = builder.eventType;
		this.action = builder.action;
		this.resourceType = builder.resourceType;
		this.resourceId = builder.resourceId;
		this.result = builder.result;
		this.details = Collections.unmodifiableMap(new HashMap<>(builder.details));
			this.traceId = builder.traceId;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public String getActorId() {
		return actorId;
	}

	public String getActorIp() {
		return actorIp;
	}

	public AuditEventType getEventType() {
		return eventType;
	}

	public String getAction() {
		return action;
	}

	public String getResourceType() {
		return resourceType;
	}

	public String getResourceId() {
		return resourceId;
	}

	public String getResult() {
		return result;
	}

	public Map<String, String> getDetails() {
		return details;
	}
	// getter
	public String getTraceId() { return traceId; }

	public static Builder builder(AuditEventType eventType, String action) {
		return new Builder(eventType, action);
	}


		public static final class Builder {
		private Instant timestamp;
		private final AuditEventType eventType;
		private final String action;
		private String actorId;
			private String traceId;
		private String actorIp;
		private String resourceType;
		private String resourceId;
		private String result;
		private final Map<String, String> details = new HashMap<>();

		public Builder(AuditEventType eventType, String action) {
			this.eventType = eventType;
			this.action = action;
		}

		public Builder timestamp(Instant timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder actorId(String actorId) {
			this.actorId = actorId;
			return this;
		}

		public Builder actorIp(String actorIp) {
			this.actorIp = actorIp;
			return this;
		}

		public Builder resource(String resourceType, String resourceId) {
			this.resourceType = resourceType;
			this.resourceId = resourceId;
			return this;
		}

		public Builder result(String result) {
			this.result = result;
			return this;
		}


		public Builder detail(String key, String value) {
			if (key != null && value != null) {
				this.details.put(key, value);
			}
			return this;
		}

			public Builder traceId(String traceId) {
				this.traceId = traceId;
				return this;
		}

		public AuditEvent build() {
			return new AuditEvent(this);
		}
	}
}

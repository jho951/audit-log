package com.auditlog.core;

import com.auditlog.api.AuditEvent;

import java.util.Map;

final class Json {
	private Json() {}

	static String toJsonLine(AuditEvent e, String serviceName, String env) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("{");

		kv(sb, "@timestamp", e.getTimestamp() != null ? e.getTimestamp().toString() : null); sb.append(",");
		kv(sb, "service", serviceName); sb.append(",");
		kv(sb, "env", env); sb.append(",");

		kv(sb, "eventType", e.getEventType() != null ? e.getEventType().name() : null); sb.append(",");
		kv(sb, "action", e.getAction()); sb.append(",");

		kv(sb, "actorId", e.getActorId()); sb.append(",");
		kv(sb, "actorIp", e.getActorIp()); sb.append(",");

		kv(sb, "resourceType", e.getResourceType()); sb.append(",");
		kv(sb, "resourceId", e.getResourceId()); sb.append(",");

		kv(sb, "result", e.getResult()); sb.append(",");
		kv(sb, "traceId", e.getTraceId());

		Map<String, String> details = e.getDetails();
		if (details != null && !details.isEmpty()) {
			sb.append(",\"details\":").append(stringMapToJson(details));
		}

		sb.append("}\n");
		return sb.toString();
	}

	private static void kv(StringBuilder sb, String k, String v) {
		sb.append("\"").append(escape(k)).append("\":");
		if (v == null) sb.append("null");
		else sb.append("\"").append(escape(v)).append("\"");
	}

	private static String stringMapToJson(Map<String, String> m) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		boolean first = true;
		for (Map.Entry<String, String> entry : m.entrySet()) {
			if (!first) sb.append(",");
			first = false;

			sb.append("\"").append(escape(entry.getKey())).append("\":");
			String val = entry.getValue();
			if (val == null) sb.append("null");
			else sb.append("\"").append(escape(val)).append("\"");
		}
		sb.append("}");
		return sb.toString();
	}

	private static String escape(String s) {
		if (s == null) return null;
		return s
			.replace("\\", "\\\\")
			.replace("\"", "\\\"")
			.replace("\n", "\\n")
			.replace("\r", "\\r");
	}
}

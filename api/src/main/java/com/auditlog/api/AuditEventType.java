package com.auditlog.api;

/**
 * 감사 이벤트 유형.
 */
public enum AuditEventType {
	LOGIN,
	LOGOUT,
	READ,
	CREATE,
	UPDATE,
	DELETE,
	SYSTEM,
	CUSTOM
}

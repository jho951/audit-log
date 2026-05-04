package com.auditlog.api.exception;

/** 공개 계약에서 구분할 최소 수준의 감사 로깅 실패 원인 */
public enum AuditFailureReason {
	/** 잘못된 이벤트 또는 호출 인자 */
	INVALID_EVENT,
	/** 잘못된 sink 또는 logger 구성 */
	INVALID_CONFIGURATION
}

package com.auditlog.api.exception;

/** 공개 계약 경계에서 사용하는 최소 수준의 감사 로그 예외 */
public class AuditException extends RuntimeException {
	private final AuditFailureReason reason;

	public AuditException(AuditFailureReason reason) {
		super(reason.name());
		this.reason = reason;
	}

	public AuditException(AuditFailureReason reason, String message) {
		super(message);
		this.reason = reason;
	}

	public AuditException(AuditFailureReason reason, String message, Throwable cause) {
		super(message, cause);
		this.reason = reason;
	}

	public AuditException(AuditFailureReason reason, Throwable cause) {
		super(reason.name(), cause);
		this.reason = reason;
	}

	public AuditFailureReason getReason() {
		return reason;
	}

	@Override
	public String toString() {
		return "AuditException{" + "reason=" + reason + ", message=" + getMessage() + '}';
	}
}

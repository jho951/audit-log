package com.auditlog.api;

/** 감사 이벤트의 주체 유형 */
public enum AuditActorType {
	/** 서비스를 이용하는 일반 사용자 */
	USER,
	/** 시스템 운영 권한을 가진 내부 직원 */
	ADMIN,
	/** 시스템이 스스로 수행한 작업 */
	SYSTEM,
	/** 외부 서비스나 다른 마이크로서비스(MSA) */
	SERVICE,
	/** 로그인하지 않은 상태의 방문자 */
	ANONYMOUS,
	/** 주체를 식별할 수 없는 비정상적인 상황이나 예외 케이스 */
	UNKNOWN
}

package com.auditlog.spi;

import java.util.Map;

/** 감사 이벤트의 민감정보를 마스킹 */
public interface AuditMaskingPolicy {
	Map<String, Object> mask(Map<String, Object> details);
}

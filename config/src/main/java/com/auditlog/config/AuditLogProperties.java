package com.auditlog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auditlog")
public class AuditLogProperties {
	private boolean enabled = true;
	private String serviceName = "unknown-service";
	private String env = "local";

	private String filePath = "./logs/audit.log";

	// ELK
	private boolean elkEnabled = false;
	private String elkEndpoint; // e.g. http://logstash:8080/audit
	private String elkApiKey;   // optional

	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public String getServiceName() { return serviceName; }
	public void setServiceName(String serviceName) { this.serviceName = serviceName; }
	public String getEnv() { return env; }
	public void setEnv(String env) { this.env = env; }
	public String getFilePath() { return filePath; }
	public void setFilePath(String filePath) { this.filePath = filePath; }
	public boolean isElkEnabled() { return elkEnabled; }
	public void setElkEnabled(boolean elkEnabled) { this.elkEnabled = elkEnabled; }
	public String getElkEndpoint() { return elkEndpoint; }
	public void setElkEndpoint(String elkEndpoint) { this.elkEndpoint = elkEndpoint; }
	public String getElkApiKey() { return elkApiKey; }
	public void setElkApiKey(String elkApiKey) { this.elkApiKey = elkApiKey; }
}

package com.auditlog.config;

import com.auditlog.api.AuditLogger;
import com.auditlog.api.AuditSink;
import com.auditlog.core.*;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;

@Configuration
@EnableConfigurationProperties(AuditLogProperties.class)
@ConditionalOnProperty(prefix = "auditlog", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuditLogAutoConfiguration {

	@Bean
	public AuditLogger auditLogger(AuditLogProperties p) {
		var sinks = new ArrayList<AuditSink>();

		sinks.add(new FileAuditSink(Path.of(p.getFilePath()), p.getServiceName(), p.getEnv()));

		if (p.isElkEnabled() && p.getElkEndpoint() != null && !p.getElkEndpoint().isBlank()) {
			sinks.add(new ElkHttpAuditSink(URI.create(p.getElkEndpoint()), p.getServiceName(), p.getEnv(), p.getElkApiKey()));
		}

		AuditSink sink = sinks.size() == 1 ? sinks.get(0) : new CompositeAuditSink(sinks);
		return new DefaultAuditLogger(sink);
	}
}

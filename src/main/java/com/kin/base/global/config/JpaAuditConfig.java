package com.kin.base.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // 이제 여기서 JPA 시간을 관리
public class JpaAuditConfig {
}

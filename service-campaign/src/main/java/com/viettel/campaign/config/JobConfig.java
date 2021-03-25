package com.viettel.campaign.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.Duration;

/**
 * @author hanv_itsol
 */

@Configuration
public class JobConfig {

    @Bean
    public ScheduledLockConfiguration taskScheduler(LockProvider lockProvider) {
        return ScheduledLockConfigurationBuilder
                .withLockProvider(lockProvider)
                .withPoolSize(10)
                .withDefaultLockAtMostFor(Duration.ofMinutes(1))
                .build();
    }

    @Bean
    public LockProvider lockProvider(@Qualifier("datasource.datasource1") DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

//    @Bean
//    public LockProvider lockProvider() {
//        JedisPool jedisPool = new JedisPool("10.60.156.82", 8013);
//        return new JedisLockProvider(jedisPool);
//    }
}

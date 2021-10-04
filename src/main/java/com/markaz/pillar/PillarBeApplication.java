package com.markaz.pillar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAspectJAutoProxy
@EnableJpaRepositories
@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
public class PillarBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PillarBeApplication.class, args);
    }
}

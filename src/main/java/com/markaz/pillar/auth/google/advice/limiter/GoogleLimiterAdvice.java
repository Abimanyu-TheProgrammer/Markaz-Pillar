package com.markaz.pillar.auth.google.advice.limiter;

import com.google.common.util.concurrent.RateLimiter;
import com.markaz.pillar.auth.google.advice.limiter.annotation.Limited;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GoogleLimiterAdvice {
    private RateLimiter limiter;

    @Autowired
    public void setLimiter(RateLimiter limiter) {
        this.limiter = limiter;
    }

    @Before(value = "@annotation(type)")
    @Order(2)
    private void validateRateLimit(JoinPoint joinPoint, Limited type) {
        limiter.acquire();
    }
}

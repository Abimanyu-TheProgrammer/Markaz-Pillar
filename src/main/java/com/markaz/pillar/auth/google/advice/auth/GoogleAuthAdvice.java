package com.markaz.pillar.auth.google.advice.auth;

import com.markaz.pillar.auth.google.advice.auth.annotation.Authenticated;
import com.markaz.pillar.auth.google.repository.GoogleTokenRepository;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Aspect
@Component
public class GoogleAuthAdvice {
    private GoogleTokenRepository repository;

    @Autowired
    public void setRepository(GoogleTokenRepository repository) {
        this.repository = repository;
    }

    @Around("@annotation(authenticated)")
    public Object validateGoogleToken(ProceedingJoinPoint joinPoint, Authenticated authenticated) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for(int i = 0; i < args.length; i++) {
            if(args[i] instanceof GoogleToken) {
                GoogleToken token = (GoogleToken) args[i];
                if(LocalDateTime.now().isAfter(token.getExpireAt())) {
                    GoogleToken refreshedToken = repository.refreshToken(
                            Optional.of(token.getAccount())
                                    .orElseThrow(() -> new IllegalStateException("Token is incorrectly formed"))
                    );

                    args[i] = refreshedToken;

                    return joinPoint.proceed(args);
                } else {
                    return joinPoint.proceed();
                }
            }
        }

        throw new IllegalArgumentException(
                String.format("@authenticated needs GoogleToken as one of the parameters at %s",
                        joinPoint.getSignature().toShortString())
        );
    }
}

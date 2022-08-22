package com.store.restapi;

import com.store.restapi.pincode.PinCodeService;
import com.store.restapi.security.refresh_token.AccountTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final AccountTokenService accountTokenService;
    private final PinCodeService pinCodeService;

    @Scheduled(cron = "@daily")
    public void clearAccountTokenService() {
        log.info("Run delete all expired account refresh token");
        accountTokenService.deleteExpiresToken();
    }

    @Scheduled(cron = "@daily")
    public void clearPinCodeService() {
        log.info("Run delete all expired pin code");
        pinCodeService.deleteAllExpired();
    }


}

package com.ohgiraffers.COZYbe.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AfterInitChecker {

    private int port;

    private final JdbcTemplate jdbcTemplate;
    private final StringRedisTemplate redisTemplate;

    private boolean isDbConnected = false;
    private boolean isRedisConnected = false;

    @EventListener
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.port = event.getWebServer().getPort();
    }


    private void checkDatabase() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            log.info("[DB] Database connection OK");
            isDbConnected = true;
        } catch (Exception e) {
            log.error("[DB] Database connection FAILED");
        }
    }

    private void checkRedis() {
        try {
            redisTemplate.opsForValue().set("__healthcheck__", "ok");
            String value = redisTemplate.opsForValue().get("__healthcheck__");
            log.info("[REDIS] Redis connection OK");
            isRedisConnected = true;
        } catch (Exception e) {
            log.error("[REDIS] Redis connection FAILED", e);
        }
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log.info("=============================================================");
        log.info("Spring Application initialising Complete");
        log.info("=============================================================");
        checkDatabase();
        checkRedis();
        printCheckResult();
    }


    private void printCheckResult(){
        System.out.println("\n ===== DB Connection Check Result ===== ");
        System.out.print("DB Status :\t");
        if (isDbConnected) {
            System.out.println("\u001B[32mSUCCESS\u001B[0m");
        }else {
            System.out.println("\u001B[31mFAILED\u001B[0m");
        }
        System.out.print("Redis Status :\t");
        if (isRedisConnected) {
            System.out.println("\u001B[32mSUCCESS\u001B[0m");
        }else {
            System.out.println("\u001B[31mFAILED\u001B[0m");
        }
        System.out.println("\nSwagger url : http://localhost:" + port + "/swagger-ui/index.html \n");
    }
}

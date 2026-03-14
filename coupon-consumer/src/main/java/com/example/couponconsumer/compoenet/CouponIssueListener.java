package com.example.couponconsumer.compoenet;

import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponIssueRequest;
import com.example.couponcore.service.CouponIssueService;
import com.example.couponcore.util.CouponRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import static com.example.couponcore.util.CouponRedisUtils.getIssueRequestQueueKey;

@RequiredArgsConstructor
@EnableScheduling
@Component
@Slf4j
public class CouponIssueListener {

    private final RedisRepository redisRepository;
    private final CouponIssueService couponIssueService;
    private final String issueRequestQueueKey = getIssueRequestQueueKey();

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Scheduled(fixedDelay = 1000L)
    public void issue() {
        log.info("Issuing coupon issue");
        while(existCouponIssueTarget()){
            CouponIssueRequest target = getIssueTarget();
            log.info("발급 시작 target: %s".formatted(target));
            couponIssueService.issue(target.couponId(), target.userId());
            log.info("발급 완료 target: %s".formatted(target));
            removeIssuedTarget();
        }
    }

    private boolean existCouponIssueTarget() {
        return redisRepository.lSize(issueRequestQueueKey) > 0;
    }

    private CouponIssueRequest getIssueTarget() {
        try {
            return objectMapper.readValue(redisRepository.lIndex(issueRequestQueueKey, 0), CouponIssueRequest.class);
        } catch (JacksonException e) {
            log.error("쿠폰 발급 요청 역직렬화 실패", e);
            throw new RuntimeException(e);
        }
    }

    private void removeIssuedTarget() {
          redisRepository.lPop(issueRequestQueueKey);
    }
}

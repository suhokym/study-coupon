package com.example.couponcore.service;

import com.example.couponcore.component.DistributeLockExecutor;
import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponIssueRequest;
import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.couponcore.exception.ErrorCode.FAIN_COUPON_ISSUE_REQUEST;
import static com.example.couponcore.util.CouponRedisUtils.getIssueRequestKey;
import static com.example.couponcore.util.CouponRedisUtils.getIssueRequestQueueKey;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV2 {


    private final RedisRepository redisRepository;
    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponCacheService couponCacheService;

    public void issue(long couponId, long userId) {
        CouponRedisEntity couponCache = couponCacheService.getCouponCache(couponId);
        couponCache.checkIssueableCoupon();
            couponIssueRedisService.checkCouponIssueQuantity(couponCache, userId);
            issueRequest(couponId, userId, couponCache.totalQuantity());


    }

    /*
    1. totalQuantity > redisRepository.sCard(key); //쿠폰 발급 수량 제어
    2. !redisRepository.sIsMember(key, String.valueOf(userId)); // 중복 발급 요청 제어
    3. redisRepository.sAdd //쿠폰 발급 요청 저장
    4. redisRepository.rPush// 쿠폰 발급 큐 적제
    */

    private void issueRequest(long couponId, long userId, Integer totalIssueQuantity) {
        if(totalIssueQuantity == null) {
            redisRepository.issueRequest(couponId, userId, Integer.MAX_VALUE);
        }
        redisRepository.issueRequest(couponId, userId, totalIssueQuantity);
    }


}

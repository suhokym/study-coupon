package com.example.couponapi;

import com.example.couponapi.dto.CouponIssueResponseDto;
import com.example.couponcore.exception.CouponIssueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CouponControllerAdvice {

    @ExceptionHandler(CouponIssueException.class)
    public CouponIssueResponseDto couponIssueExceptionHandler(CouponIssueException e){
        log.error("coupon issue error: {}", e.getMessage(), e);
        return new CouponIssueResponseDto(false,e.getErrorCode().message);
    }
}

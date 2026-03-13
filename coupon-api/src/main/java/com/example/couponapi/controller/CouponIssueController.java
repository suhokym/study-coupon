package com.example.couponapi.controller;

import com.example.couponapi.dto.CouponIssueRequestDto;
import com.example.couponapi.dto.CouponIssueResponseDto;
import com.example.couponapi.service.CouponIssueRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CouponIssueController {

    private final CouponIssueRequestService couponIssueRequestService;

    @PostMapping("/v1/issue")
    public CouponIssueResponseDto issueRequestV1(@RequestBody CouponIssueRequestDto requestDto){
        couponIssueRequestService.issueRequestV1(requestDto);
        return new CouponIssueResponseDto(true, null);
    }

    @PostMapping("/v1/issue-asynk")
    public CouponIssueResponseDto asynkIssueV1(@RequestBody CouponIssueRequestDto requestDto){
        couponIssueRequestService.asynkIssueRequestV1(requestDto);
        return new CouponIssueResponseDto(true, null);
    }

}

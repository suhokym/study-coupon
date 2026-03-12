package com.example.couponcore;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@EnableJpaAuditing
@Component
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example.couponcore")
public class CouponCoreConfiguration {


}

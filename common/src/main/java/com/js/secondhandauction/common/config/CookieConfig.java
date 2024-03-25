package com.js.secondhandauction.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

//@Configuration
public class CookieConfig {
    //aws 로그인 세션을 공유하지 않아, 해당 소스 주석처리
//    @Bean
//    public CookieSerializer cookieSerializer() {
//        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//        serializer.setDomainName("localhost");
//        serializer.setUseSecureCookie(false);
//        serializer.setUseHttpOnlyCookie(false);
//        return serializer;
//    }
//
//    @Bean
//    public HttpSessionIdResolver httpSessionIdResolver() {
//        CookieHttpSessionIdResolver resolver = new CookieHttpSessionIdResolver();
//        resolver.setCookieSerializer(cookieSerializer());
//        return resolver;
//    }
}

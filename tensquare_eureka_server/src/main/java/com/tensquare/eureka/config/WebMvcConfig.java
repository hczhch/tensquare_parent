package com.tensquare.eureka.config;

import com.tensquare.eureka.constants.FilterOrderConstants;
import com.tensquare.eureka.filter.VersionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import javax.servlet.DispatcherType;

@Configuration
public class WebMvcConfig {

    @Bean
    public FilterRegistrationBean<VersionFilter> versionFilterRegistration(VersionFilter versionFilter) {
        FilterRegistrationBean<VersionFilter> registrationBean = new FilterRegistrationBean<>(versionFilter);
        registrationBean.setOrder(FilterOrderConstants.VERSION_ORDER);
        registrationBean.setUrlPatterns(Collections.singletonList("/version"));
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR);
        return registrationBean;
    }

}

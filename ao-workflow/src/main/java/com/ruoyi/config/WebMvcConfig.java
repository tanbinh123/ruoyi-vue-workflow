package com.ruoyi.config;

import com.ruoyi.config.properties.SysProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
@ConditionalOnMissingBean(WebMvcConfigurer.class)
public class WebMvcConfig implements WebMvcConfigurer {

    private final SysProperties sysProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bpmn/**").addResourceLocations("file:" + sysProperties.getBpmnLocation());
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}

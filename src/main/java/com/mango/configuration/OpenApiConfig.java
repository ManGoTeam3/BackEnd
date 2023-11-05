package com.mango.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI WhatssueOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Durian-Team5 OpenApi")
                        .description("망고플레이트 클론 프로젝트")
                        .version("V0.0.1")
                        .contact(new Contact()
                                .name("mago")
//                                .email("")
                        ))
                .externalDocs(new ExternalDocumentation()
                        .description("Mango Github")
                        .url("https://github.com/ManGoTeam3"));
    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
}


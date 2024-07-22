package com.bizzy.skillbridge.webMvcConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://lisungui-client.s3-website.eu-north-1.amazonaws.com",
                                "http://lisungui-client.s3-website.eu-north-1.amazonaws.com/"
                                // "https://lemon-smoke-0d0925a0f.5.azurestaticapps.net",
                                // "http://localhost:3000",
                                // "https://lisungui.onrender.com"
                                )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

package ch.hsr.apparch.purchaselist.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestLoggingConfiguration {

    @Bean
    public RequestLoggingFilter logFilter() {
        return new RequestLoggingFilter();
    }
}

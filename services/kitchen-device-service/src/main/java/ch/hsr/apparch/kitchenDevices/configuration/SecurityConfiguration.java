package ch.hsr.apparch.kitchenDevices.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    protected void configure(HttpSecurity http) throws Exception {
         http.authorizeRequests().anyRequest().permitAll().and().cors().and().csrf().disable();
    }
}

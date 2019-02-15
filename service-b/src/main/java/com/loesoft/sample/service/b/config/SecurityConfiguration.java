package com.loesoft.sample.service.b.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .antMatchers("/insecure").permitAll()
                .antMatchers("/secure").hasAuthority("SCOPE_doBthings")
                .anyRequest().denyAll()
                .and()
            .oauth2ResourceServer()
                .jwt();
        // @formatter:on
    }

}
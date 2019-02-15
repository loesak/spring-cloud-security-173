package com.loesoft.sample.service.a.config;

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
                .antMatchers("/insecure*").permitAll()
                .antMatchers("/secure*").hasAuthority("SCOPE_doAthings")
                .anyRequest().denyAll()
                .and()
            .oauth2ResourceServer()
                .jwt()
                .and().and()
            .oauth2Client();
        // @formatter:on
    }

//    @Bean
//    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor(
//            final OAuth2ClientContext oAuth2ClientContext,
//            final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails) {
//        return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, oAuth2ProtectedResourceDetails);
//    }
}
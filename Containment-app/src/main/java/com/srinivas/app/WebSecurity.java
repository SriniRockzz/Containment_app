package com.srinivas.app;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception { 
       
        http.authorizeRequests()
        .antMatchers("/dashboard","/").authenticated()
        .anyRequest().permitAll()
        .and()
        .formLogin()
            .defaultSuccessUrl("/dashboard")
            .permitAll()
        .and()
        .logout().logoutSuccessUrl("/login").permitAll();
    }
    
}
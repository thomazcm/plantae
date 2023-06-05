package com.thomazcm.plantae.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations {

    @Autowired
    private AutenticacaoService autenticacaoService;
    
    @Value("${plantae.endpoint.apiEndpoint}")
    private String apiEndpoint;
    
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http
                .authorizeRequests()
                .antMatchers("/login/**","/logout/**","/js/**","/css/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		                .invalidSessionUrl("/login") 
		                .maximumSessions(1) 
		                .expiredUrl("/login"))
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl(apiEndpoint, true)
                        .failureUrl("/login")
                        .permitAll())
                .headers().frameOptions().sameOrigin();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
    
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(autenticacaoService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
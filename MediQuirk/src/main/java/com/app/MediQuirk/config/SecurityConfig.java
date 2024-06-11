package com.app.MediQuirk.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.builder()
                .username("user")
                .password("{bcrypt}$2a$10$.YDFmma42WlHBJuh.xP2N.Mo7gDmJ.og2P1Wtz1ZaDYk//DY3CQwm")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$.YDFmma42WlHBJuh.xP2N.Mo7gDmJ.og2P1Wtz1ZaDYk//DY3CQwm")
                .roles("USER","ADMIN")
                .build();
        return  new InMemoryUserDetailsManager(user,admin) ;
    }
}

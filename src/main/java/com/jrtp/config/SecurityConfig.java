package com.jrtp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class SecurityConfig {
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.httpBasic().and()
				.authorizeRequests()
				//.anyRequest().authenticated()
				//.mvcMatchers("/**").permitAll()
				.and().authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/adduser").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest()
                .authenticated()                  
                .and()
                .csrf().disable()
                .headers().frameOptions().disable().and().build();
				
	}
    @Bean
    public UserDetailsService userDetailsService() {
    	var uds= new InMemoryUserDetailsManager();
    	var user1= User.withUsername("arjun")
    			.password(passwordEncoder().encode("12345"))
    			.authorities("read","write")
    			.build();
    	
    	uds.createUser(user1);
    	return uds;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}

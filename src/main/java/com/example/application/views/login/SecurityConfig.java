package com.example.application.views.login;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        super.configure(http);



        setLoginView(http, LoginView.class);

                //Jostain syystä tämä ei toimi

                /*http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/dog-adoption", "/person-form", "/images/**").permitAll()
                        .anyRequest().authenticated()
                );*/

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/dog-adoption")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login");

    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin@example.com")
                .password("{noop}password123")
                .roles("EMPLOYER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

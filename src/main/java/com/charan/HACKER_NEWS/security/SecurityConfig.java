package com.charan.HACKER_NEWS.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select username, password, enabled from users where username=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username, role from users where username=?");
        return jdbcUserDetailsManager;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/posts/list");
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests(authorizeRequests ->
//                                authorizeRequests.requestMatchers("/posts/showFormForAdd/**").authenticated()
////                                .requestMatchers("/posts/list").authenticated()
//                                        .anyRequest().permitAll()
//                )
//        http
//                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
//                        .requestMatchers("/api/posts/**").permitAll()
//                        .requestMatchers("/api/posts/**").hasAnyRole("AUTHOR", "ADMIN")
//                        .requestMatchers("/api/user/save").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/user/LoginPage")
//                                .loginProcessingUrl("/authenticateUser")
//                                .successHandler(successHandler())
//                                .permitAll()
//                )
//                .logout(logout ->
//                        logout
//                                .logoutUrl("/logout")
//                                .logoutSuccessUrl("/posts/list")
//                                .permitAll()
//                )
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling
//                                .accessDeniedPage("/access-denied")
//                );
//
//        return http.build();
//    }
        http
                .authorizeRequests(authorizeRequests ->
                                authorizeRequests.requestMatchers("/posts/showFormForAdd/**").authenticated()
                                .requestMatchers("/comments/list").authenticated()
                                        .anyRequest().permitAll()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/user/LoginPage")
                                .loginProcessingUrl("/authenticateUser")
                                .successHandler(successHandler())
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/posts/list")
                                .permitAll()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}

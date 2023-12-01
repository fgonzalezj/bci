package com.bci.users.auth;

import com.bci.users.services.UsersService;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Value("${spring.security.jwt.secret}")
  private String jwtSecret;

  private final UsersService usersService;

  public SecurityConfig(UsersService usersService) {
    this.usersService = usersService;
  }

  @Bean
  public static BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecretKey secretKey() {
    return Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // TODO configure authentication manager
    auth.userDetailsService(usersService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // TODO:Remove when JWT is active
    http.csrf()
        .disable()
        .authorizeRequests()
            // remove the path '/login' to enable authentication through access_token
        .antMatchers("/oautqh/token", "/sign-up", "/login")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(
            new JwtTokenFilter("a7ZwvHhJ6759kb3EZS/TKXzCl59Qpz6K5AMxvQlDtnY="),
            UsernamePasswordAuthenticationFilter.class);
    http.headers().frameOptions().disable();
  }

  @Override
  @Bean
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }
}

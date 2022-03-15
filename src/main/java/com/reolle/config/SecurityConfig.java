package com.reolle.config;

import com.reolle.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/login", "/sign-up", "check-email-token",
                        "/email-login", "login-by-email").permitAll()
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
                .anyRequest().authenticated();

        //로그인 페이지는 예외
        http.formLogin()
                .loginPage("/login").permitAll();

        http.logout()
                .logoutSuccessUrl("/"); //로그아웃 할경우 어디로갈지

        //쿠키탈취당했을 경우 유효하지않은 토큰 및 시리즈 username 제공해서 피해 최소화
        http.rememberMe()
                .userDetailsService(accountService)
                .tokenRepository(tokenRepository());
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    //스태틱리소스들은 걍패스시켜라라
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                //로그인 후 no message availabe 떳을때 밑에 한줄 추가
                .antMatchers("/favicon.ico", "/resources/**")
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}

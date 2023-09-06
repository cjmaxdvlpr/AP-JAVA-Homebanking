package com.mindhub.homebanking.configurations;


import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static java.lang.Long.parseLong;


@EnableWebSecurity
@Configuration
//public class WebAuthorization extends WebSecurityConfigurerAdapter {
public class WebAuthorization {

    @Autowired
    ClientRepository clientRepository;
    //@Override
    //protected void configure(HttpSecurity http) throws Exception {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                //.antMatchers("/").permitAll()
                //.antMatchers("/web/index.html").permitAll()
                .antMatchers("/web/index.html", "/web/js/**", "/web/css/**", "/web/img/**").permitAll()
                //.antMatchers("/api/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .antMatchers("/api/login", "/api/logout").permitAll()
                //.antMatchers("/web/accounts.html", "/web/js/**", "/web/css/**", "/web/img/**").hasAuthority("CLIENT")
                //.antMatchers("/api/clients/{id}").access()
                //.antMatchers("/api/accounts/{id}").access()
                .antMatchers("/api/accounts/**").hasAuthority("CLIENT")
                .antMatchers("/api/transactions/**").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers("/api/**").hasAuthority("ADMIN")
                //.antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/h2-console").hasAuthority("ADMIN");
                //.anyRequest().denyAll();


        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");


        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        //httpSecurity.headers().frameOptions().disable();
        //HttpSecurity.headers().frameOptions().disable();
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    //Example: Validate variable path with access method
    //https://github.com/spring-projects/spring-security/issues/3781
    //public boolean checkAccountId(Authentication authentication, String accountId) {
    /*public boolean checkAccountId(String userName, String accountId) {
        //String userName = authentication.getName();
        Client client = clientRepository.findByEmail(userName);
        List<Long> clientAccountIds = client.getAccountsIds();
        Long accId = parseLong(accountId);
        return clientAccountIds.contains(accId);
    }*/

}

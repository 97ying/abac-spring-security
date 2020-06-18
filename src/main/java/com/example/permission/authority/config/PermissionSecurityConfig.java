package com.example.permission.authority.config;

import com.example.permission.authority.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class PermissionSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService);
//    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        final List<AuthenticationProvider> providers = new ArrayList<>(1);
        providers.add(preAuthProvider());
        return new ProviderManager(providers);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
        http.addFilterAfter(userHeaderFilter(), RequestHeaderAuthenticationFilter.class).authorizeRequests()
                .anyRequest().authenticated();
//                .and().httpBasic();
//                .antMatchers("/", "/**").permitAll();
        http.csrf().disable();
    }

    @Bean
    public RequestHeaderAuthenticationFilter userHeaderFilter() throws Exception {
        RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
        requestHeaderAuthenticationFilter.setPrincipalRequestHeader("userId");
        requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return requestHeaderAuthenticationFilter;
    }

    @Bean
    PreAuthenticatedAuthenticationProvider preAuthProvider() throws Exception {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
        return provider;
    }

    @Bean
    UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() throws Exception {
        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
        wrapper.setUserDetailsService(userService);
        return wrapper;
    }
}

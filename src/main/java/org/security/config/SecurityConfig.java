package org.security.config;

import org.security.filters.JwtAuthenticationFilter;
import org.security.filters.JwtAuthorizationFilter;
import org.security.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // normalement cette class herite de la class WebSecurityConfigureAdapter mais cette dernier est deprecacted
    private final UserDetailService userDetailService;

    public SecurityConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        // spécifier les authorisation sur les resource, c'est la technique classic ou on peut utiliser les annotation
        //http.authorizeRequests().antMatchers(HttpMethod.POST,"/users/**").hasAnyAuthority("ADMIN");
        //http.authorizeRequests().antMatchers(HttpMethod.GET,"/users/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/h2console/**", "/refreshToken/**", "/login/**", "/createComptes/**").permitAll();
        http.formLogin();
        http.authorizeRequests().anyRequest().authenticated();
        // add filtre to config
        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
        // vu qu'on utilise plusieur filtre spécifié a spring sécurity que le filtre d'authorisation celui qui reçoi a requet , aussi on spécifié le type de filtre
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

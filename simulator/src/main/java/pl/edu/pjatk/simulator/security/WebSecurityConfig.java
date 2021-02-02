package pl.edu.pjatk.simulator.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/users/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET,"/**").hasAnyAuthority("ROLE_ADMIN, ROLE_MANAGER, ROLE_USER")
                .antMatchers("/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                .anyRequest().authenticated()
                .and()
                    .addFilter(new TokenAuthenticationFilter(authenticationManager()))
                    .addFilter(new TokenAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
    }
}
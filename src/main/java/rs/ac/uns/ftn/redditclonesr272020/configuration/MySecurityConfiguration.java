package rs.ac.uns.ftn.redditclonesr272020.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.oauth2Login();
//        httpSecurity.csrf().disable();
        httpSecurity
                .authorizeRequests()
                .antMatchers("/h2-console/*").permitAll()
                .antMatchers("/", "/webjars/**").permitAll();
//                .anyRequest().authenticated();
        httpSecurity.logout()
                .logoutSuccessUrl("/").permitAll();
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

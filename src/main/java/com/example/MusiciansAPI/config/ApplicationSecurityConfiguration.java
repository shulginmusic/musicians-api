package com.example.MusiciansAPI.config;

import com.example.MusiciansAPI.service.CustomUserDetailsService;
import com.example.MusiciansAPI.security.JwtAuthenticationEntryPoint;
import com.example.MusiciansAPI.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//REFERENCE: https://www.callicoder.com/series/spring-security-react/

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity( //Enable method level security (see controller classes)
//        securedEnabled = true, //It enables the @Secured annotation
//        jsr250Enabled = true, // It enables the @RolesAllowed
//        prePostEnabled = true //enables @PreAuthorize and @PostAuthorize
//)
public class ApplicationSecurityConfiguration  extends WebSecurityConfigurerAdapter {

    //Custom User Details Service
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    //Unauthorized exception handler
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    //JWT Filter
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    //Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * AuthenticationManagerBuilder is used to create an AuthenticationManager instance which is the main
     * Spring Security interface for authenticating a user.
     *
     * You can use AuthenticationManagerBuilder to build in-memory authentication,
     * LDAP authentication, JDBC authentication, or add your custom authentication provider.
     *
     * In our example, we’ve provided our customUserDetailsService and a passwordEncoder
     * to build the AuthenticationManager.
     *
     * We’ll use the configured AuthenticationManager to authenticate a user in the login API.
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        //H2 Console access
//        http.authorizeRequests().antMatchers("/h2-console/**").permitAll()
//                .and().csrf().ignoringAntMatchers("/h2-console/**")
//                .and().headers().frameOptions().sameOrigin();

        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/api/auth/**",
                        "/test-advice"
                        )
                .permitAll()
                .antMatchers( "/api/protected/**")
                .hasRole("USER")
                .anyRequest()
                .authenticated();
        
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}


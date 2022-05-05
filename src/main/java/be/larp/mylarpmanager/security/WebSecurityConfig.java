package be.larp.mylarpmanager.security;

import be.larp.mylarpmanager.security.jwt.AuthEntryPointJwt;
import be.larp.mylarpmanager.security.jwt.AuthTokenFilter;
import be.larp.mylarpmanager.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/renew").authenticated()
                .antMatchers("/api/v1/auth/whoami").authenticated()
                .antMatchers("/api/v1/auth/logout").authenticated()
                .antMatchers("/api/v1/auth/changepassword").authenticated()
                .antMatchers("/api/v1/auth/signoff").authenticated()
                .antMatchers("/api/v1/auth/massivesignoff").authenticated()
                .antMatchers("/api/v1/user/changedetails").authenticated()
                .antMatchers("/api/v1/user/getmycharacters").authenticated()
                .antMatchers("/api/v1/user/setrole").authenticated()
                .antMatchers("/api/v1/user/create").authenticated()
                .antMatchers("/api/v1/character/changedetails").authenticated()
                .antMatchers("/api/v1/character/create").authenticated()
                .antMatchers("/api/v1/character/getpointsavailable").authenticated()
                .antMatchers("/api/v1/character/delete").authenticated()
                .antMatchers("/api/v1/character/addskill").authenticated()
                .antMatchers("/api/v1/character/kill").authenticated()
                .antMatchers("/api/v1/nation/changedetails").authenticated()
                .antMatchers("/api/v1/nation/getmynationplayers").authenticated()
                .antMatchers("/api/v1/nation/getallnations").authenticated()
                .antMatchers("/api/v1/nation/getmynationrequests").authenticated()
                .antMatchers("/api/v1/nation/forcejoinnation").authenticated()
                .antMatchers("/api/v1/nation/joinnation").authenticated()
                .antMatchers("/api/v1/nation/leavenation").authenticated()
                .antMatchers("/api/v1/nation/processdemand").authenticated()
                .antMatchers("/api/v1/nation/getmynation").authenticated()
                .antMatchers("/api/v1/nation/create").authenticated()
                .antMatchers("/api/v1/skill/changedetails").authenticated()
                .antMatchers("/api/v1/skill/getallskilltreeskills").authenticated()
                .antMatchers("/api/v1/skill/create").authenticated()
                .antMatchers("/api/v1/skill/delete").authenticated()
                .antMatchers("/api/v1/skilltree/changedetails").authenticated()
                .antMatchers("/api/v1/skilltree/getallskilltrees").authenticated()
                .antMatchers("/api/v1/skilltree/create").authenticated()
                .antMatchers("/api/v1/skilltree/getall").authenticated()
                .antMatchers("/api/v1/event/create").authenticated()
                .antMatchers("/api/v1/event/changedetails").authenticated()
                .antMatchers("/api/v1/event/participate").authenticated()
                .antMatchers("/api/v1/event/setchosenone").authenticated()
                .antMatchers("/api/v1/event/setsummary").authenticated()
                .antMatchers("/api/v1/point/create").authenticated()
                .antMatchers("/api/v1/point/changedetails").authenticated()
                .anyRequest().permitAll();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}

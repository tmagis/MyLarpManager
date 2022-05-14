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
                .antMatchers("/api/v1/auth/whoAmI").authenticated()
                .antMatchers("/api/v1/auth/logout").authenticated()
                .antMatchers("/api/v1/auth/changePassword").authenticated()
                .antMatchers("/api/v1/auth/signOff").authenticated()
                .antMatchers("/api/v1/auth/massiveSignOff").authenticated()
                .antMatchers("/api/v1/user/changeDetails").authenticated()
                .antMatchers("/api/v1/user/getMyCharacters").authenticated()
                .antMatchers("/api/v1/user/setRole").authenticated()
                .antMatchers("/api/v1/user/create").authenticated()
                .antMatchers("/api/v1/character/changeDetails").authenticated()
                .antMatchers("/api/v1/character/create").authenticated()
                .antMatchers("/api/v1/character/getPointsAvailable").authenticated()
                .antMatchers("/api/v1/character/").authenticated()
                .antMatchers("/api/v1/character/addSkill").authenticated()
                .antMatchers("/api/v1/character/removeSkill").authenticated()
                .antMatchers("/api/v1/character/kill").authenticated()
                .antMatchers("/api/v1/nation/changeDetails").authenticated()
                .antMatchers("/api/v1/nation/getMyNationPlayers").authenticated()
                .antMatchers("/api/v1/nation/getAllNations").authenticated()
                .antMatchers("/api/v1/nation/getMyNationRequests").authenticated()
                .antMatchers("/api/v1/nation/forceJoinNation").authenticated()
                .antMatchers("/api/v1/nation/joinNation").authenticated()
                .antMatchers("/api/v1/nation/leaveNation").authenticated()
                .antMatchers("/api/v1/nation/processDemand").authenticated()
                .antMatchers("/api/v1/nation/getMyNation").authenticated()
                .antMatchers("/api/v1/nation/create").authenticated()
                .antMatchers("/api/v1/skill/changeDetails").authenticated()
                .antMatchers("/api/v1/skill/getAllSkillTreeSkills").authenticated()
                .antMatchers("/api/v1/skill/create").authenticated()
                .antMatchers("/api/v1/skill/").authenticated()
                .antMatchers("/api/v1/skillTree/changeDetails").authenticated()
                .antMatchers("/api/v1/skillTree/create").authenticated()
                .antMatchers("/api/v1/skillTree/getAll").authenticated()
                .antMatchers("/api/v1/event/create").authenticated()
                .antMatchers("/api/v1/event/changeDetails").authenticated()
                .antMatchers("/api/v1/event/participate").authenticated()
                .antMatchers("/api/v1/event/setChosenOne").authenticated()
                .antMatchers("/api/v1/event/setSummary").authenticated()
                .antMatchers("/api/v1/point/create").authenticated()
                .antMatchers("/api/v1/point/changeDetails").authenticated()
                .anyRequest().permitAll();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}

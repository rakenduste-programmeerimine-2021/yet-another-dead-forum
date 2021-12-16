package ee.tlu.forum.security;

import ee.tlu.forum.filter.CustomAuthenticationFilter;
import ee.tlu.forum.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // changing default /login path to /api/login
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        // Default config tracks user by cookies, we will use JWT instead.
        http.csrf().disable().cors(); // disabled cross-site request forgery && enable cors
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // allow to use swagger without logging in (at least for now)
        http.authorizeRequests().
                antMatchers(AUTH_WHITELIST).permitAll();

        // UserController
        http.authorizeRequests().antMatchers("/api/user/delete/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/api/user/edit/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers("/api/users/****").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers("/api/user/****").hasAnyAuthority("ROLE_USER");

        // RoleController
        http.authorizeRequests().antMatchers(GET, "/api/roles/****").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/role/****").hasAnyAuthority("ROLE_ADMIN");

        // ThreadController
        http.authorizeRequests().antMatchers("/api/thread/delete/****").hasAnyAuthority("ROLE_ADMIN", "ROLE_MODERATOR");
        http.authorizeRequests().antMatchers("/api/thread/add/****").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers("/api/thread/edit/****").hasAnyAuthority("ROLE_USER");

        // PostController
        http.authorizeRequests().antMatchers("/api/post/delete/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MODERATOR");
        http.authorizeRequests().antMatchers("/api/post/add/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers("/api/post/edit/**").hasAnyAuthority("ROLE_USER");

        // MaintenanceController
        http.authorizeRequests().antMatchers("/api/maintenance/edit/**").hasAnyAuthority("ROLE_ADMIN");

        // allow every other URL to every non-logged in user
        http.authorizeRequests().antMatchers("/api/**").permitAll();

        http.authorizeRequests().anyRequest().authenticated(); // need to be authenticated

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };
}

package ee.tlu.forum.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.tlu.forum.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager; // authenticates the actual user

    @Value("${jwt.secret}")
    private String jwtSecret;

    UserService userService;
//    Environment environment;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // first requests the actual given parameters (Now in Json, wee!)
        ObjectMapper mapper = new ObjectMapper();
        UsernamePasswordForm loginInfo = new UsernamePasswordForm();

        try {
            loginInfo = mapper.readValue(request.getInputStream(), UsernamePasswordForm.class);
        } catch(IOException e) {
            log.error("Something went wrong! " + e);
        }

        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();
        log.info("Authentication attempt, Username: {}, Password: {}", username, password);

        // then create an authentication token with the provided parameters
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // then authenticationManager attempts to authenticate. If successful, calls successfulAuthentication method.
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    // Gives the user the token after authentication
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        // The below 'User' class belongs to UserDetails (same class name)
        User user = (User) auth.getPrincipal(); // returns the user that's been authenticated

        /*
        hack to inject UserService
        https://stackoverflow.com/questions/32494398/unable-to-autowire-the-service-inside-my-authentication-filter-in-spring/32495757
         */
        if (userService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            userService = webApplicationContext.getBean(UserService.class);
        }

        //https://stackoverflow.com/questions/30528255/how-to-access-a-value-defined-in-the-application-properties-file-in-spring-boot
        // put together this hack by combining answers from above + the previous hack
        if (jwtSecret == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            jwtSecret = webApplicationContext.getBean(org.springframework.core.env.Environment.class).getProperty("jwt.secret");
        }

        ee.tlu.forum.model.User userInfo = userService.getUserByUsername(user.getUsername());

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Algorithm is from the Java JTW library. We input a secret here.
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
        String accessToken = JWT.create()
                .withSubject(user.getUsername()) // needs a string that's unique to the user so it can identify the user by the specific token
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // expires in 60 minutes
                .withIssuer(request.getRequestURL().toString()) // author of token, eg url of app
                .withClaim("roles", roles) // pass in the roles
                .sign(algorithm); // sign it with the selected algorithm and secret

//         Parse the tokens as json
        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", accessToken);
        tokens.put("id", String.valueOf(userInfo.getId()));
        tokens.put("username", userInfo.getUsername());
        tokens.put("roles", roles.toString());
        tokens.put("email", userInfo.getEmail());
        tokens.put("about", userInfo.getEmail());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}

@Data
class UsernamePasswordForm {
    private String username;
    private String password;
}

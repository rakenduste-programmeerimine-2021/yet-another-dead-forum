package ee.tlu.forum.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager; // authenticates the actual user

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager1) {
        this.authenticationManager = authenticationManager1;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // first requests the actual given parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
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

        // Algorithm is from the Java JTW library. We input a secret here.
        Algorithm algorithm = Algorithm.HMAC256("secretmwahahaWahahahahHWhaaaaa".getBytes(StandardCharsets.UTF_8));
        String accessToken = JWT.create()
                .withSubject(user.getUsername()) // needs a string that's unique to the user so it can identify the user by the specific token
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // expires in 10 minutes
                .withIssuer(request.getRequestURL().toString()) // author of token, eg url of app
                .withClaim("roles", user.getAuthorities() // pass in all the roles of the user
                        .stream() // map with stream API
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining()))
                .sign(algorithm); // sign it with the selected algorithm and secret

        String refreshToken = JWT.create()
                .withSubject(user.getUsername()) // needs a string that's unique to the user so it can identify the user by the specific token
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // expires in one hour
                .withIssuer(request.getRequestURL().toString()) // author of token, eg url of app
                .sign(algorithm); // sign it with the selected algorithm and secret

//        response.setHeader("access_token", accessToken);
//        response.setHeader("refresh_token", refreshToken);

        // Parse the tokens as json
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}

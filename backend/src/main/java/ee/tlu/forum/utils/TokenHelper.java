package ee.tlu.forum.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import ee.tlu.forum.exception.BadRequestException;
import ee.tlu.forum.exception.NoPermissionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class TokenHelper {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (Exception e) {
            throw new NoPermissionException("Invalid credentials");
        }
    }

    public void hasRoleOrUsername(String token, String username, String[] requiredRoles) {
        String throwMessage = "Forbidden";
        DecodedJWT decodedJWT = this.decodeToken(token);
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        if (decodedJWT.getSubject().equals(username) || this.hasAnyRole(requiredRoles, roles)) {
            return;
        }
        if (!decodedJWT.getSubject().equals(username)) {
            throwMessage = "Unauthorized action";
        }
        throw new NoPermissionException(throwMessage);
    }

    public boolean hasAnyRole(String[] requiredRoles, String[] givenRoles) {
        for (String required : requiredRoles) {
            if (Arrays.asList(givenRoles).contains(required)) {
                return true;
            }
        }
        return false;
    }
}

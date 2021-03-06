package be.larp.mylarpmanager.security.jwt;

import be.larp.mylarpmanager.models.uuid.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${warden.app.jwtSecret}")
    private String jwtSecret;

    @Value("${warden.app.tokenValidityInMinutes:15}")
    private int tokenValidityInMinutes;

    public String generateJwtToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.create()
                .withIssuer("Warden")
                .withClaim("user", userPrincipal.getUuid())
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusMinutes(tokenValidityInMinutes).toInstant()))
                .sign(algorithm);
    }

    public String getUuidFromJwtToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.require(algorithm)
                .withIssuer("Warden")
                .build()
                .verify(token)
                .getClaim("user")
                .asString();
    }

    public boolean validateJwtToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        try {
            JWT.require(algorithm)
                    .withIssuer("Warden")
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException ignored) {
        }
        return false;
    }

}

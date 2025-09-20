package com.example.chatserver.authentication;

import com.example.chatserver.user.User;
import com.example.chatserver.user.UserDTO;
import com.example.chatserver.user.UserRepository;
import com.example.chatserver.user.UserService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationService {
    private static final Log log = LogFactory.getLog(AuthenticationService.class);

    protected static final String SIGNER_KEY = "poLBWPgVQou48bsluXo1KZxpjR+eCY7PsXisVtfkqu+7Z6qWeRazpVPrU4Uf8i9s";

    @Autowired
    private UserRepository userRepository;

    public AuthenticationResponse Login(Authentication authentication) {
        //Check if user already exists
        if(!userRepository.existsByUsername(authentication.getUsername())) {
            throw new RuntimeException("User not exists");
        }

        if (authentication.getPassword() == null || authentication.getPassword().isEmpty()) {
            System.out.println("[UserService] Password is null or empty: '" + authentication.getPassword() + "'");
            throw new RuntimeException("Password cannot be empty");
        }

        User user = userRepository.findByUsername(authentication.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if(passwordEncoder.matches(authentication.getPassword(), user.getPassword())) {
            var token = genToken(authentication.getUsername());
            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        }
        else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public String genToken(String username) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet ClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("SKWinG")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();
        Payload payload = new Payload(ClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("[UserService] Error while generating token: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspectToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            Date expTime = jwsObject.getPayload().toJSONObject().get("exp") == null ? new Date(0) : new Date((Long) jwsObject.getPayload().toJSONObject().get("exp") * 1000);

            return IntrospectResponse.builder()
                    .active(jwsObject.verify(verifier) && expTime.after(new Date()))
                    .build();
        } catch (Exception e) {
            log.error("[UserService] Error while introspecting token: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

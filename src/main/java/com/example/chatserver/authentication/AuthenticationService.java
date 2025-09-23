package com.example.chatserver.authentication;

import com.example.chatserver.invalidJWT.InvalidToken;
import com.example.chatserver.invalidJWT.InvalidTokenRspository;
import com.example.chatserver.user.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticationService {
    private static final Log log = LogFactory.getLog(AuthenticationService.class);

    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvalidTokenRspository invalidTokenRspository;

    public UserDTO createUser(Authentication authentication) {
        //Check if user already exists
        if(userRepository.existsByUsername(authentication.getUsername())) {
            throw new RuntimeException("User already exists");
        }

        if (authentication.getPassword() == null || authentication.getPassword().isEmpty()) {
            System.out.println("[UserService] Password is null or empty: '" + authentication.getPassword() + "'");
            throw new RuntimeException("Password cannot be empty");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String hashedPassword = passwordEncoder.encode(authentication.getPassword());

        User user = new User();
        user.setUsername(authentication.getUsername());
        user.setPassword(hashedPassword);

        userRepository.save(user);
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();
    }

    public AuthenticationResponse Login(Authentication authentication) {
        //Check if user already exists
        if(!userRepository.existsByUsername(authentication.getUsername())) {
            throw new RuntimeException("User not exists");
        }

        if (authentication.getPassword() == null || authentication.getPassword().isEmpty()) {
            System.out.println("[UserService] Password is null or empty: '" + authentication.getPassword() + "'");
            throw new RuntimeException("Password cannot be empty");
        }

        User user = userRepository.findByUsername(authentication.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
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

    //Hàm tạo token
    public String genToken(String username) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("SKWinG")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
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

    public void logout(String jti, Long exp) {
        // Invalidate the token by storing its jti in a blacklist (not implemented here)
        System.out.println("[AuthenticationService] Logging out token with jti: " + jti + " and exp: " + exp);
        // You can implement a token blacklist using a database or in-memory store like Redis
        try {        // giây
            Instant expInstant = Instant.ofEpochSecond(exp);
            invalidTokenRspository.save(new InvalidToken(jti, expInstant));
        } catch (NumberFormatException e) {
            log.error("[AuthenticationService] Invalid exp format: " + exp);
        }
    }
}

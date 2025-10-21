package com.example.chatserver.security;

import com.example.chatserver.authentication.IntrospectResponse;
import com.example.chatserver.invalidJWT.InvalidTokenRspository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;

    @Autowired
    private InvalidTokenRspository invalidTokenRspository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("INCOMING REQUEST: " + request.getMethod() + " " + request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
//        System.out.println("Authorization header: " + authHeader); // Log header
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove 'Bearer '
        }
//        System.out.println("Extracted token: " + token); // Log token

        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //Kiểm tra token có bị thu hồi không
        String jti = (String) jwsObject.getPayload().toJSONObject().get("jti");
        if (invalidTokenRspository.existsById(jti)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token has been revoked");
            return;
        }

        if (token == null || !introspectToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token");
            return;
        }

        // Set SecurityContext để Spring Security nhận diện đã xác thực
        String username = (String) jwsObject.getPayload().toJSONObject().get("sub");
        UserPrincipal userPrincipal = new UserPrincipal(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Chuyển tiếp nếu xác thực thành công
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // Các đường dẫn này sẽ được filter bỏ qua, không kiểm tra token
        return path.startsWith("/api/auth") // Bỏ qua tất cả API xác thực
                || path.equals("/")
                || path.equals("/index.html")
                || path.equals("/login.html")
                || path.equals("/register.html")
                || path.startsWith("/css/")  // Bỏ qua tất cả file trong thư mục /css/
                || path.startsWith("/js/")   // Bỏ qua tất cả file trong thư mục /js/
                || path.startsWith("/ws");   // Bỏ qua WebSocket handshake endpoint
    }


    public boolean introspectToken(String token) {
        try {
            // Cắt "Bearer " nếu token truyền từ header
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            JWSObject jwsObject = JWSObject.parse(token);
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            Object expObj = jwsObject.getPayload().toJSONObject().get("exp");
            Date expTime = new Date(0);
            if (expObj instanceof Number) {
                long expSeconds = ((Number) expObj).longValue();
                expTime = new Date(expSeconds * 1000);
            }

//            System.out.println("expTime: " + expTime);
//            System.out.println("now: " + new Date());
//            System.out.println("verify: " + jwsObject.verify(verifier));

            return jwsObject.verify(verifier) && expTime.after(new Date());
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

}
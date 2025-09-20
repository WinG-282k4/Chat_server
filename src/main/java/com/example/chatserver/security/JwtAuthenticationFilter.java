//package com.example.chatserver.security;
//
//import com.nimbusds.jose.JOSEException;
//import com.nimbusds.jose.JWSObject;
//import com.nimbusds.jose.crypto.MACVerifier;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.Data;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.text.ParseException;
//import java.util.Date;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private static final String SIGNER_KEY = "poLBWPgVQou48bsluXo1KZxpjR+eCY7PsXisVtfkqu+7Z6qWeRazpVPrU4Uf8i9s";
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//
//            try {
//                JWSObject jwsObject = JWSObject.parse(token);
//
//                // Kiểm tra thời gian hết hạn ở đây nếu cần thiết
//                Date expirationTime = jwsObject.getPayload().toJSONObject().get("exp") != null ?
//                        new Date(((Number) jwsObject.getPayload().toJSONObject().get("exp")).longValue() * 1000) : null;
//
//                boolean verified = jwsObject.verify(new MACVerifier(SIGNER_KEY.getBytes(StandardCharsets.UTF_8))) &&
//                                   (expirationTime == null || expirationTime.after(new Date()));;
//                if (!verified) {
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    response.getWriter().write("Invalid JWT signature");
//                    return;
//                }
//
//                // Lấy payload JSON
//                String payload = jwsObject.getPayload().toString();
//                request.setAttribute("jwtPayload", payload);
//
//            } catch (ParseException | JOSEException e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid JWT token");
//                return;
//            }
//        } else {
//            // Nếu endpoint cần token mà không có, có thể bỏ qua nếu endpoint public
//            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            // response.getWriter().write("Missing Authorization header");
//            // return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
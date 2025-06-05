package com.ecommerce.util;
import java.nio.charset.StandardCharsets;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
 
import org.springframework.stereotype.Service;
 
 
 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
 
@Service
public class JwtUtil {
 
    // Your preset secret value (should be loaded from configuration in production)
    public static final String SECRET = "ca704dd4af8d2058f2ba429d610db35348d7dfcabd123b240add2045fd7f97eaccc5b11b69423e6e668331cb11133985b3af1667ccf9d8e5753acb46414cf00a";
 
    
 
    public void validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
         Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

package com.example.KindRaise.jwt;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

    @Value("${spring.jwt.secret.key}")
    private String jwtKey;

    public String generateTokenWithUsername(String username){
        String access_token=Jwts
        .builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis()+1000*60*60*24))
        .signWith(this.key())
        .compact();
        return access_token;
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtKey));
    }

    public Boolean validateToken(String token){
        try {
            Jwts.parser().verifyWith(this.key()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println(e);
           return false;
        }
    }

    public String extractUserIdFromToken(String token){
        return Jwts.parser().verifyWith(this.key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public String extractTokenFromHeader(HttpServletRequest httpServletRequest){
        String token=httpServletRequest.getHeader("Authorization");
        if(token!=null&&token.startsWith("Bearer")){
            return token.substring(7);
        }
        return null;
    }


}

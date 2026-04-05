package com.hhgcl.security;



import com.hhgcl.entity.User;
import com.hhgcl.exceptions.UnAuthorizedException;
import com.hhgcl.repository.UserRepository;
import com.hhgcl.utils.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JwtService {
    private  final UserRepository userRepository;

    private final SecurityProperties properties;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(properties.getSecret())
        );
    }


    public String generateToken(User user) {

        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + properties.getExpiration());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", List.of(user.getRole()))
                .claim("username", user.getUsername())
                .claim("expiry", expiryDate.getTime())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + properties.getExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}

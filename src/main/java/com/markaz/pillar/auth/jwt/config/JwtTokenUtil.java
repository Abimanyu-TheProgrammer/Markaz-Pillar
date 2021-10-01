package com.markaz.pillar.auth.jwt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Setter
public class JwtTokenUtil {
    @Value("${service.jwt.token.length}")
    private long tokenLength;

    @Value("${service.jwt.token.skew}")
    private long tokenSkew;

    @Value("${service.name}")
    private String issuer;

    @Value("${service.jwt.token.secret}")
    private String jwtSecret;

    //    Exclude Expiration date exception
    public String getSubject(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //    Exclude Expiration date exception
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //    Exclude Expiration date exception
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //    Exclude Expiration date exception
    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .setAllowedClockSkewSeconds(tokenSkew)
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //    Exclude Expiration date exception
    //check if the token has expired
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        userDetails.getAuthorities()
                .forEach(grantedAuthority -> {
                    String authority = grantedAuthority.getAuthority();
                    if(authority.startsWith("ROLE_")) {
                        claims.put("role", authority);
                    } else {
                        ((ArrayList<String>) claims.computeIfAbsent("menu", s -> new ArrayList<>())).add(authority);
                    }
                });

        return doGenerateToken(claims, userDetails.getUsername());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenLength * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String generateRefreshToken(UserDetails user, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);

        byte[] bytes = md.digest(user.getUsername().getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for(byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();

    }

    //validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (userDetails.getUsername().equals(email) && !isTokenExpired(token));
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .setAllowedClockSkewSeconds(tokenSkew)
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}

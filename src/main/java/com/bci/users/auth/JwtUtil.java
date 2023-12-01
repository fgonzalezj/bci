package com.bci.users.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  @Value("${spring.security.jwt.secret}")
  private String secretKey;

  @Value("${spring.security.jwt.expiration}")
  private String expirationTime;

  public String generateToken(String subject) {
    byte[] secretBytes = java.util.Base64.getDecoder().decode(secretKey);
    Key secretKey = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());

    return Jwts.builder()
        .setSubject(subject)
        .setExpiration(new Date(System.currentTimeMillis() + Long.valueOf(expirationTime) * 1000))
        .signWith(secretKey)
        .compact();
  }
}

package com.bridgelabz.fundoo.utility;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Component
public class JWTProvider {

    private final String secret = "Gautam";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

    private final Algorithm algorithm = Algorithm.HMAC512(secret);

    public String generateToken(String emailId) {
	return JWT.create().withClaim("email", emailId).withIssuedAt(Date.from(Instant.now()))
		.withExpiresAt(Date.from(Instant.now().plusMillis(EXPIRATION_TIME))).sign(algorithm);
    }

    public String getEmailFromToken(String token) {
	try {
	    return JWT.require(algorithm).build().verify(token).getClaim("email").asString();
	} catch (JWTVerificationException e) {
	    throw new RuntimeException("Invalid or expired JWT token", e);
	}
    }

    public boolean validateToken(String token) {
	try {
	    JWT.require(algorithm).build().verify(token);
	    return true;
	} catch (JWTVerificationException e) {
	    return false;
	}
    }
}

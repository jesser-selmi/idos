package com.IDOSdigital.userManagement.security.token;

import com.IDOSdigital.userManagement.entities.User;
import com.IDOSdigital.userManagement.repositories.UserRepository;
import com.IDOSdigital.userManagement.security.CustomUserDetails;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenProcessor {
    private final Logger logger = LoggerFactory.getLogger(TokenProcessor.class);
    @Autowired
    private JwtConfiguration jwtConfiguration;
    @Autowired
    private UserRepository userRepository;
    private static final String EMAIL = "sub";
    public Authentication authenticate(HttpServletRequest request) {
        String token = request.getHeader(jwtConfiguration.getHttpHeader());
        if (token != null && token.startsWith("Bearer ")) {
            String bearerToken = token.substring(7);
            try {
                JWTClaimsSet claims = getClaimsFromToken(bearerToken);
                if (claims != null) {
                    String email = claims.getStringClaim(EMAIL);
                    User user = userRepository.findUserByEmail(email).orElse(null);
                    if (user != null) {
                        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
                        UserDetails userDetails = new org.springframework.security.core.userdetails.User(email, "", authorities);
                        return new JwtAuthentication(userDetails, claims, authorities);
                    }
                }
            } catch (Exception e) {
                logger.error("Token processing error", e);
            }
        }
        return null;
    }

    private JWTClaimsSet getClaimsFromToken(String token) {
        try {
            return JWTParser.parse(token).getJWTClaimsSet();
        } catch (Exception e) {
            logger.error("Error parsing JWT token", e);
            return null;
        }
    }

    private void validateIssuer(JWTClaimsSet claims) throws IssuerMismatchException {
        String expectedIssuer = jwtConfiguration.getJwtSecret();
        String actualIssuer = claims.getIssuer();
        if (!expectedIssuer.equals(actualIssuer)) {
            throw new IssuerMismatchException(String.format("Issuer %s does not match expected %s", actualIssuer, expectedIssuer));
        }
    }

    public String generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUserId();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfiguration.getExpirationTime());

        try {
            JWSSigner signer = new MACSigner(jwtConfiguration.getJwtSecret().getBytes());
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toList());
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .issuer(jwtConfiguration.getJwtIssuer())
                    .expirationTime(expiryDate)
                    .claim("roles", roles)
                    .claim("userId", userId)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );
            signedJWT.sign(signer);
            return "Bearer " + signedJWT.serialize();
        } catch (Exception e) {
            logger.error("Token generation error", e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    public static class IssuerMismatchException extends Exception {
        public IssuerMismatchException(String message) {
            super(message);
        }
    }
}

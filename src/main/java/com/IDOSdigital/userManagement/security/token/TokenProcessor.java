package com.IDOSdigital.userManagement.security.token;

import com.IDOSdigital.userManagement.entities.User;
import com.IDOSdigital.userManagement.repositories.UserRepository;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Base64;
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
import java.util.Optional;

import static java.util.List.of;

@Component
@Transactional
public class TokenProcessor {

    private final Logger logger = LoggerFactory.getLogger(TokenProcessor.class);

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL = "email";

    public Authentication authenticate(HttpServletRequest request) throws Exception {
        String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
        if (idToken != null) {
            HttpSession session = request.getSession();
            session.setAttribute("idToken", idToken);
            JWTClaimsSet claims = this.getClaimsFromToken(this.getBearerToken(idToken));
            if (claims != null) {
                validateIssuer(claims);
                if (claims.getClaims().get(EMAIL) != null) {
                    final String email = claims.getClaims().get(EMAIL).toString();
                    User user = userRepository.findUserByEmail(email).orElse(null);
                    if (user != null) {
                        session.setAttribute("userId", claims.getClaims().get("sub"));
                        final String role = "ROLE_".concat(user.getRole().name());
                        final List<GrantedAuthority> grantedAuthorities = of(new SimpleGrantedAuthority(role));
                        final UserDetails userDetails = new org.springframework.security.core.userdetails.User(email, "", grantedAuthorities);
                        return new JwtAuthentication(userDetails, claims, grantedAuthorities);
                    }
                }
            }
        }
        return null;
    }

    private JWTClaimsSet getClaimsFromToken(String bearerToken) {
        try {
            JWT jwt = JWTParser.parse(bearerToken);
            return jwt.getJWTClaimsSet();
        } catch (final Exception e) {
            logger.error("Error : ", e);
            return null;
        }
    }

    private String getBearerToken(String token) {
        return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
    }

    private void validateIssuer(JWTClaimsSet claims) throws IssuerMismatchException {
        Base64 base64 = new Base64();
        final String decodedIssuer = new String(base64.decode(claims.getIssuer().getBytes()));

        if (!decodedIssuer.equals(this.jwtConfiguration.getJwtSecret())) {
            throw new IssuerMismatchException(String.format("Issuer %s does not match deeply idp %s", claims.getIssuer(), this.jwtConfiguration.getJwtSecret()));
        }
    }

    public class IssuerMismatchException extends Exception {
        public IssuerMismatchException(String message) {
            super(message);
        }
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfiguration.getExpirationTime());

        try {
            JWSSigner signer = new MACSigner(jwtConfiguration.getJwtSecret().getBytes());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .issuer("Nzg3ZjA1ZjUtNzEyMy00ZmEzLWFjMjAtYWU2ZGVkZjJhOWZi")
                    .expirationTime(expiryDate)
                    .claim("roles", userDetails.getAuthorities())
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );
            signedJWT.sign(signer);
            return "Bearer " + signedJWT.serialize();
        } catch (Exception e) {
            logger.error("Token generation error", e);
            throw new RuntimeException("Token generation failed");
        }
    }
}

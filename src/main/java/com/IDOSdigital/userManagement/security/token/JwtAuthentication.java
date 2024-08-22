package com.IDOSdigital.userManagement.security.token;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@Getter
public class JwtAuthentication extends AbstractAuthenticationToken {

    private final transient  Object principal;
    private final JWTClaimsSet jwtClaimsSet;

    public JwtAuthentication(Object principal, JWTClaimsSet jwtClaimsSet, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.jwtClaimsSet = jwtClaimsSet;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return null;
    }

}





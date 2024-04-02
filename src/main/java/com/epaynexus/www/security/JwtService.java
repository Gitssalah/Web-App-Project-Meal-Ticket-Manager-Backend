package com.epaynexus.www.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.epaynexus.www.service.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import io.jsonwebtoken.JwtParser;

@AllArgsConstructor
@Service
public class JwtService {

    private static final String ENCRYPTION_KEY = "8a276db52598c6051fb8ee7211312f958ae39dd43dd62e3251bea788ab973d63";
    private UtilisateurService utilisateurService;
    private List<String> invalidatedTokens = new ArrayList<>();

    public Map<String, String> generate(String username) {
        UserDetails utilisateur = this.utilisateurService.loadUserByUsername(username);
        Long userID= this.utilisateurService.extractUserIDByUsername(username);
        return this.generateJwt(utilisateur, userID);
    }

    private Map<String, String> generateJwt(UserDetails utilisateur, Long userID) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;

        final Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.EXPIRATION, new Date(expirationTime));
        claims.put(Claims.SUBJECT, utilisateur.getUsername());
        claims.put("roles", getRolesFromUserDetails(utilisateur));

        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getUsername())
                .addClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("bearer", bearer, "userID", userID.toString());
    }

    private String getRolesFromUserDetails(UserDetails userDetails) {
        StringBuilder rolesBuilder = new StringBuilder();
        userDetails.getAuthorities().forEach(authority -> rolesBuilder.append(authority.getAuthority()).append(","));
        String roles = rolesBuilder.toString();
        return roles.isEmpty() ? roles : roles.substring(0, roles.length() - 1);
    }

    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public String lireUSername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpire(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(getKey()).build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(getKey()).build();
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return invalidatedTokens.contains(token);
    }
    
}

package sasf.net.app.security;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenUtils {
	
	private final static String ACCES_TOKEN_SECRET = "scretKeyscretKeyscretKeyscretKey";
	private final static Long ACCES_TOKEN_VALIDITY_SECONDS = 592_000L;

	public static String createToken(String name, String email,String  role) {
		Date expirationDate = new Date(System.currentTimeMillis() + ACCES_TOKEN_VALIDITY_SECONDS);
		
		Map<String, Object> extra = new HashMap<>();
		extra.put("name", name);
		extra.put("role", role);
		
		return Jwts.builder()
				.setSubject(email)
				.setExpiration(expirationDate)
				.addClaims(extra)
				.signWith(Keys.hmacShaKeyFor(ACCES_TOKEN_SECRET.getBytes()))//firmar el token 
				.compact();
	}

	public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(ACCES_TOKEN_SECRET.getBytes())
					.build()
					.parseClaimsJws(token)
					.getBody();
			String email = claims.getSubject();
			return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

		} catch (JwtException e) {
			System.out.println(e);
			return null;
		}

	}
}

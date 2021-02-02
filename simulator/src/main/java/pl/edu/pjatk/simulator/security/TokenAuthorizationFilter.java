package pl.edu.pjatk.simulator.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.pjatk.simulator.security.util.SecurityConstants.*;


public class TokenAuthorizationFilter extends BasicAuthenticationFilter {

    public TokenAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(SECRET))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            String user = decoded.getSubject();
            List<GrantedAuthority> authorities = decoded.getClaim("aut").asList(String.class).stream()
                    .map(s -> (GrantedAuthority) () -> s)
                    .collect(Collectors.toList());

            if (user != null) {

                return new UsernamePasswordAuthenticationToken(user, user, authorities);
            }
            return null;
        }

        return null;
    }
}

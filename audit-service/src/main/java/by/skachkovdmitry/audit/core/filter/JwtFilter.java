package by.skachkovdmitry.audit.core.filter;


import by.dmitryskachkov.entity.TokenError;
import by.skachkovdmitry.audit.core.security.UserSecurity;
import by.skachkovdmitry.audit.core.utils.JwtTokenHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.Tolerate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtTokenHandler jwtHandler;

    public JwtFilter(JwtTokenHandler jwtHandler) {
        this.jwtHandler = jwtHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {


        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }


        try {


            // Get jwt token and validate
            final String token = header.split(" ")[1].trim();
            if (!jwtHandler.validate(token)) {
                chain.doFilter(request, response);
                return;
            }


            UserSecurity userSecurity = jwtHandler.getUser(token);


            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userSecurity, null,
                    userSecurity == null ?
                            List.of() : userSecurity.getAuthorities()
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (TokenError e) {
            handleVerificationError(response, e);
        }
    }

    private void handleVerificationError(HttpServletResponse response, TokenError e) throws IOException {
        response.setStatus(e.getHttpStatusCode().value());
        response.setContentType("application/json");
        String jsonError = "{\"error\": \"" + e.getMessage() + "\"}";
        response.getWriter().write(jsonError);
        response.getWriter().flush();
    }
}
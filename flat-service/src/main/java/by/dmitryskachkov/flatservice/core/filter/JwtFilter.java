package by.dmitryskachkov.flatservice.core.filter;

import by.dmitryskachkov.entity.TokenError;

import by.dmitryskachkov.flatservice.core.dto.UserSecurity;
import by.dmitryskachkov.flatservice.core.utils.JwtTokenHandler;
import by.dmitryskachkov.flatservice.service.api.IUserService;
import feign.RetryableException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenHandler jwtHandler;

    private final IUserService userService;

    public JwtFilter(JwtTokenHandler jwtHandler, IUserService userService) {
        this.jwtHandler = jwtHandler;
        this.userService = userService;
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
            User user = userService.getStatus(header);
            System.out.println(user.toString());
            if (!Objects.equals(user.getStatus(), "ACTIVATED")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                String jsonError = "{\"error\": \"Your account is blocked. Please contact support.\"}";
                response.getWriter().write(jsonError);
                response.getWriter().flush();
                return;
            }
        } catch (RetryableException e) {
            log.error("нет доступа к микросервису user-service для проверки статуса");
            chain.doFilter(request, response);
            return;
        }




        final String token = header.split(" ")[1].trim();
        try {
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
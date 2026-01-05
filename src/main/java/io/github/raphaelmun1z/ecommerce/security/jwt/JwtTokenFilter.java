package io.github.raphaelmun1z.ecommerce.security.jwt;

import io.github.raphaelmun1z.ecommerce.exceptions.models.InvalidJwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver resolver;

    public JwtTokenFilter(JwtTokenProvider tokenProvider, HandlerExceptionResolver resolver) {
        this.tokenProvider = tokenProvider;
        this.resolver = resolver;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
        throws IOException, ServletException {
        try {
            String token = tokenProvider.resolverToken((HttpServletRequest) request);

            if (StringUtils.isNotBlank(token) && tokenProvider.validarToken(token)) {
                Authentication authentication = tokenProvider.obterAutenticacao(token);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (InvalidJwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
        }

        filter.doFilter(request, response);
    }
}
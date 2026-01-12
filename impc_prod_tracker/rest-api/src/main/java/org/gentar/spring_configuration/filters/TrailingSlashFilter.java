package org.gentar.spring_configuration.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1) // Run early in the filter chain, before Spring Security
public class TrailingSlashFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // Remove trailing slash except for root path
        if (requestURI.length() > 1 && requestURI.endsWith("/")) {
            String normalizedURI = requestURI.substring(0, requestURI.length() - 1);

            // Wrap the request to override path-related methods
            HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
                @Override
                public String getRequestURI() {
                    return normalizedURI;
                }

                @Override
                public String getServletPath() {
                    String servletPath = super.getServletPath();
                    if (servletPath.length() > 1 && servletPath.endsWith("/")) {
                        return servletPath.substring(0, servletPath.length() - 1);
                    }
                    return servletPath;
                }

                @Override
                public String getPathInfo() {
                    String pathInfo = super.getPathInfo();
                    if (pathInfo != null && pathInfo.length() > 1 && pathInfo.endsWith("/")) {
                        return pathInfo.substring(0, pathInfo.length() - 1);
                    }
                    return pathInfo;
                }
            };

            chain.doFilter(wrappedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}


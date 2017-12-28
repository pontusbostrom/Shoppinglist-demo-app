package org.vaadin.pontus.shoppinglist.rest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain arg2) throws IOException, ServletException {
        try {
            Authentication auth = SecurityContextHolder.getContext()
                    .getAuthentication();
            String name = auth == null ? null : auth.getName();
            String mdcData = String.format("[userId:%s]", name);
            MDC.put("mdcData", mdcData);
            arg2.doFilter(arg0, arg1);
        } finally {
            MDC.clear();
        }

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}

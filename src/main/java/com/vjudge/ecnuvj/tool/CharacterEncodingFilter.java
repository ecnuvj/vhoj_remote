package com.vjudge.ecnuvj.tool;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import java.io.IOException;

public class CharacterEncodingFilter implements Filter {

    protected String encoding = "UTF-8";

    @Override
    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (!StringUtils.isBlank(encoding)) {
            servletRequest.setCharacterEncoding(encoding);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
    }

}

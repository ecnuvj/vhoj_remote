package com.vjudge.ecnuvj.tool;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter implementation class PDFFileRedirect
 */
public class PDFFileRedirect implements Filter {

    /**
     * Default constructor.
     */
    public PDFFileRedirect() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        /* 获取客户端请求的url */
        String url = request.getRequestURI();
        // log.info(url);

        if (url.contains("/contest/pdf/")) {
            response.sendRedirect(url.replace("/contest/pdf/", "/problem/pdf/"));
        } else {
            chain.doFilter(req, res);
        }

    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}

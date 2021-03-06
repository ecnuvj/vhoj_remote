package com.vjudge.ecnuvj.tool;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MyFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(MyFilter.class);

    private SessionContext myc;

    private ForbiddenVisitorRuler forbiddenVisitorRuler;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (forbiddenVisitorRuler == null) {
            forbiddenVisitorRuler = SpringBean.getBean("forbiddenVisitorRuler", ForbiddenVisitorRuler.class);
        }

        try {

            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            HttpSession session = request.getSession();

            String ua = request.getHeader("user-agent");

            // get X-Real-IP if the request comes from nginx
            String ip = request.getHeader("X-Forwarded-For");
            if (StringUtils.isEmpty(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (StringUtils.isEmpty(ip)) {
                ip = request.getRemoteAddr();
            }

            if (forbiddenVisitorRuler.forbidden(ua, ip)) {
                myc = SessionContext.getInstance();
                session.invalidate();
                myc.DelSession(session);

                response.sendRedirect("http://ip138.com/ips138.asp?ip=" + ip.replaceAll(",.+", ""));
                return;
            }

            if (session.getAttribute("remoteAddr") == null) {
                session.setAttribute("remoteAddr", ip);
            }
            if (session.getAttribute("user-agent") == null) {
                session.setAttribute("user-agent", request.getHeader("user-agent"));
            }
            if (session.getAttribute("referer") == null) {
                String host = request.getHeader("Host");
                String referer = request.getHeader("referer");
                if (!StringUtils.isBlank(referer) && !StringUtils.isBlank(host) && !referer.contains(host)) {
                    session.setAttribute("referer", referer);
                }
            }
            if (session.getAttribute("country") == null) {
                session.setAttribute("country", req.getLocale().getCountry());
            }

            chain.doFilter(req, res);

//            Enumeration paramNames = request.getParameterNames();
//            while (paramNames.hasMoreElements()) {
//                String paramName = (String) paramNames.nextElement();
//                System.out.print(paramName + "=");
//                String[] paramValues = request.getParameterValues(paramName);
//                for (String value : paramValues) {
//                    System.out.print(value + " ");
//                }
//                System.out.println();
//            }
//            log.info("");

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


}
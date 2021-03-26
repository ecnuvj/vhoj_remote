package com.vjudge.ecnuvj.tool;

import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void addCookie(ActionContext actionContext, String name, String value) {
        addCookie(actionContext, name, value, 60 * 60 * 24 * 365);
    }

    public static void addCookie(ActionContext actionContext, String name, String value, int maxAge) {
        HttpServletResponse response = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void removeCookie(ActionContext actionContext, String name) {
        addCookie(actionContext, name, "", 0);
    }

    public static String getCookie(ActionContext actionContext, String name) {
        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}

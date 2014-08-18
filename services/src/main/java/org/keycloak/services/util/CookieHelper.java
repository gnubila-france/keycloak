package org.keycloak.services.util;

import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CookieHelper {

    /**
     * Set a response cookie.  This solely exists because JAX-RS 1.1 does not support setting HttpOnly cookies
     *
     * @param name
     * @param value
     * @param path
     * @param domain
     * @param comment
     * @param maxAge
     * @param secure
     * @param httpOnly
     */
    public static void addCookie2(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly) {
        HttpResponse response = ResteasyProviderFactory.getContextData(HttpResponse.class);
        StringBuffer cookieBuf = new StringBuffer();
        ServerCookie.appendCookieValue(cookieBuf, 1, name, value, path, domain, comment, maxAge, secure, httpOnly);
        String cookie = cookieBuf.toString();
        response.getOutputHeaders().add(HttpHeaders.SET_COOKIE, cookie);
    }

    public static void addCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly) {
        HttpServletResponse response = ResteasyProviderFactory.getContextData(HttpServletResponse.class);
        Cookie cookie = new Cookie(name, value);
        if (path != null) cookie.setPath(path);
        if (domain != null) cookie.setDomain(domain);
        if (comment != null) cookie.setComment(comment);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);

        response.addCookie(cookie);

    }


}

package com.viettel.campaign.filter;

import com.viettel.campaign.utils.RedisUtil;
import com.viettel.econtact.filter.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    //private static final Logger logger = LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Authorization, X-Requested-With, Content-Type, Accept, token1, X-Auth-Token, Message");
        response.setHeader("Access-Control-Expose-Headers", "Message");

        HttpServletRequest request = (HttpServletRequest) req;

//        chain.doFilter(req, response);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, resp);
            return;
        }
        if ("/".equals(request.getRequestURI()) || request.getRequestURI().startsWith("/checkConnection/")) {
            chain.doFilter(request, resp);
            return;
        }
        String xAuthToken = request.getHeader("X-Auth-Token");
        if (xAuthToken == null || "".equals(xAuthToken)) {
            //logger.info("--- CORS ERR: " + "The token is null.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is null.");
            return;
        }
        Object obj = RedisUtil.getInstance().get(xAuthToken);
        if (obj instanceof UserSession) {
            chain.doFilter(request, resp);
        } else {
            //logger.info("--- CORS ERR: " + "The token is invalid.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is invalid.");
        }
    }

    @Override
    public void destroy() {

    }
}

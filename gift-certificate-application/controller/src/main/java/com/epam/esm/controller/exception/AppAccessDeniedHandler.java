package com.epam.esm.controller.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class AppAccessDeniedHandler implements AccessDeniedHandler {
    public static final String URI_STRING = "uri";
    public static final String MSG_STRING = "msg";
    public static final String TIMESTAMP_STRING = "timestamp";
    public static final String ACCESS_DENIED_MSG = "Access denied";

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        map.put(URI_STRING, request.getRequestURI());
        map.put(MSG_STRING, ACCESS_DENIED_MSG);
        map.put(TIMESTAMP_STRING, String.valueOf(LocalDateTime.now()));
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ResponseBodyPreparator.prepare(response, map);
    }
}

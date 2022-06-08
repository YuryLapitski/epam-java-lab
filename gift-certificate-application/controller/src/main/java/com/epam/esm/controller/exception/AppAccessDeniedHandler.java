package com.epam.esm.controller.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AppAccessDeniedHandler implements AccessDeniedHandler {
    private static final String URI_STRING = "uri";
    private static final String MSG_STRING = "msg";
    private static final String TIMESTAMP_STRING = "timestamp";
    private static final String ACCESS_DENIED_MSG = "Access denied";

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ResponseFiller.fill(response, createResponseBody(request));
    }

    private Map<String, String> createResponseBody(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String localDateTimeNow = LocalDateTime.now().toString();

        Map<String, String> map = new HashMap<>();
        map.put(URI_STRING, requestUri);
        map.put(MSG_STRING, ACCESS_DENIED_MSG);
        map.put(TIMESTAMP_STRING, localDateTimeNow);

        return map;
    }
}

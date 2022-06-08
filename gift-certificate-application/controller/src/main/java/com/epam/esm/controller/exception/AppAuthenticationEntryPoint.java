package com.epam.esm.controller.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String URI_STRING = "uri";
    private static final String MSG_STRING = "msg";
    private static final String TIMESTAMP_STRING = "timestamp";
    private static final String UNAUTHORIZED_MSG = "You are unauthorized";
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseFiller.fill(response, createResponseBody(request));
    }

    private Map<String, String> createResponseBody(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String localDateTimeNow = LocalDateTime.now().toString();

        HashMap<String, String> map = new HashMap<>();
        map.put(URI_STRING, requestUri);
        map.put(MSG_STRING, UNAUTHORIZED_MSG);
        map.put(TIMESTAMP_STRING, localDateTimeNow);

        return map;
    }
}

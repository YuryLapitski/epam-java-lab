package com.epam.esm.controller.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String URI_STRING = "uri";
    private static final String MSG_STRING = "msg";
    private static final String TIMESTAMP_STRING = "timestamp";
    private static final String UNAUTHORIZED_MSG = "You are unauthorized";
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        map.put(URI_STRING, request.getRequestURI());
        map.put(MSG_STRING, UNAUTHORIZED_MSG);
        map.put(TIMESTAMP_STRING, String.valueOf(LocalDateTime.now()));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseBodyPreparator.prepare(response, map);
    }
}

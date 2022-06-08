package com.epam.esm.controller.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public final class ResponseFiller {
    private static final String CHARACTER_ENCODING = "utf-8";

    private ResponseFiller() {
    }

    public static void fill(HttpServletResponse response, Map<String, String> map) throws IOException {
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(map);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(responseBody);
        printWriter.flush();
        printWriter.close();
    }
}

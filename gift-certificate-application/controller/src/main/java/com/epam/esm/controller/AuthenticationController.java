package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.AuthenticationRequestDto;
import com.epam.esm.service.dto.AuthenticationResponseDto;
import com.epam.esm.controller.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserService userService,
                                    JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public AuthenticationResponseDto authenticate(@RequestBody AuthenticationRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(),
                request.getPassword()));
        User user = userService.findByLogin(request.getLogin());
        String token = jwtTokenProvider.createToken(user.getLogin(), user.getRole().name());

        return AuthenticationResponseDto.builder().username(request.getLogin()).token(token).build();
    }
}

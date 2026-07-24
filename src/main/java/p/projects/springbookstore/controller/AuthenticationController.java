package p.projects.springbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import p.projects.springbookstore.dto.UserLoginRequestDto;
import p.projects.springbookstore.dto.UserLoginResponseDto;
import p.projects.springbookstore.dto.UserRegistrationRequestDto;
import p.projects.springbookstore.dto.UserResponseDto;
import p.projects.springbookstore.exception.LoginException;
import p.projects.springbookstore.exception.RegistrationException;
import p.projects.springbookstore.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "Register a new user",
            description = "Registers a new user in the system")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user",
            description = "Logs in a user and returns a JWT token")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request)
            throws LoginException {
        return authenticationService.login(request);
    }
}

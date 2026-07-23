package p.projects.springbookstore.service;

import p.projects.springbookstore.dto.UserLoginRequestDto;
import p.projects.springbookstore.dto.UserLoginResponseDto;
import p.projects.springbookstore.dto.UserRegistrationRequestDto;
import p.projects.springbookstore.dto.UserResponseDto;

public interface AuthenticationService {

    public UserResponseDto register(UserRegistrationRequestDto request);

    public UserLoginResponseDto login(UserLoginRequestDto request);
}

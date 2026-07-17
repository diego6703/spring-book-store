package p.projects.springbookstore.service;

import p.projects.springbookstore.dto.UserRegistrationRequestDto;
import p.projects.springbookstore.dto.UserResponseDto;

public interface AuthenticationService {

    public UserResponseDto register(UserRegistrationRequestDto request);
}

package p.projects.springbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p.projects.springbookstore.dto.UserRegistrationRequestDto;
import p.projects.springbookstore.dto.UserResponseDto;
import p.projects.springbookstore.exception.RegistrationException;
import p.projects.springbookstore.mapper.UserMapper;
import p.projects.springbookstore.model.User;
import p.projects.springbookstore.repository.UserRepository;
import p.projects.springbookstore.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("User with this email already exists");
        }

        User user = userMapper.toEntity(request);
        return userMapper.toDto(userRepository.save(user));
    }
}

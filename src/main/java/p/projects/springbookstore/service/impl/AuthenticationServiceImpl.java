package p.projects.springbookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import p.projects.springbookstore.dto.UserRegistrationRequestDto;
import p.projects.springbookstore.dto.UserResponseDto;
import p.projects.springbookstore.exception.EntityNotFoundException;
import p.projects.springbookstore.exception.RegistrationException;
import p.projects.springbookstore.mapper.UserMapper;
import p.projects.springbookstore.model.Role;
import p.projects.springbookstore.model.RoleName;
import p.projects.springbookstore.model.User;
import p.projects.springbookstore.repository.RoleRepository;
import p.projects.springbookstore.repository.UserRepository;
import p.projects.springbookstore.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RegistrationException("User with this email already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role USER"));
        user.setRoles(Set.of(userRole));

        return userMapper.toDto(userRepository.save(user));
    }
}

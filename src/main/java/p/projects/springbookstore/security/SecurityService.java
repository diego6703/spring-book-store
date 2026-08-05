package p.projects.springbookstore.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import p.projects.springbookstore.model.User;
import p.projects.springbookstore.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by email: " + email));
    }

    public Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }
}

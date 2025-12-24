package com.cites.agent.infrastructure.security;

import com.cites.agent.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
            .map(UserPrincipal::new)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado: " + email)
            );
    }
}

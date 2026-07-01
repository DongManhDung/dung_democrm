package com.dung.democrm.auth.security;

import com.dung.democrm.entity.User;
import com.dung.democrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        validateUserStatus(user);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                ).build();
    }

    // Kiểm tra trạng thái tài khoản
    private void validateUserStatus(User user){
        switch (user.getStatus()){
            case ACTIVE -> {
                // Cho phép đăng nhập
            }

            case ON_LEAVE -> {
                // Vẫn cho login
            }

            case LOCKED -> {
                throw new LockedException("Your account has been locked");
            }

            case RESIGNED -> {
                throw new DisabledException("Your account has been deactivated");
            }
        }
    }
}

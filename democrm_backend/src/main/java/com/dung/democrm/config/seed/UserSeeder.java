package com.dung.democrm.config.seed;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.entity.User;
import com.dung.democrm.repository.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class UserSeeder {
    private final Dotenv dotenv;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void seed(){
        if(userRepository.count() > 0) return;

        userRepository.save(createAdmin());

        IntStream.rangeClosed(1,30)
                .forEach(i -> userRepository.save(createManager(i)));

        IntStream.rangeClosed(1,510)
                .forEach(i -> userRepository.save(createSales(i)));
    }

    private User createAdmin(){
        User user = new User();
        user.setFullName("System Admin");
        user.setEmail(dotenv.get("ADMIN_EMAIL") + dotenv.get("EMAIL_DOMAIN"));
        user.setPassword(passwordEncoder.encode(dotenv.get("ADMIN_PASSWORD")));
        user.setRole(Role.ADMIN);

        return user;
    }

    private User createManager(int index){
        User user = new User();
        user.setFullName("Sales Manager " + index);
        user.setEmail(dotenv.get("MANAGER_EMAIL_NAME") + index + dotenv.get("EMAIL_DOMAIN"));
        user.setPassword(passwordEncoder.encode(dotenv.get("MANAGER_PASSWORD")));
        user.setRole(Role.MANAGER);

        return user;
    }

    private User createSales(int index){
        User user = new User();
        user.setFullName("Sales Employee " + index);
        user.setEmail(dotenv.get("SALES_EMAIL_NAME") + index + dotenv.get("EMAIL_DOMAIN"));
        user.setPassword(passwordEncoder.encode(dotenv.get("SALES_PASSWORD")));
        user.setRole(Role.SALES);

        return user;
    }
}

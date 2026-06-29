package com.dung.democrm.config.seed;

import com.dung.democrm.common.enums.Role;
import com.dung.democrm.common.enums.UserStatus;
import com.dung.democrm.entity.User;
import com.dung.democrm.repository.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class UserSeeder {

    private static final int TOTAL_MANAGERS = 30;
    private static final int TOTAL_SALES = 510;

    private final Dotenv dotenv;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    public void seed(){
        if(userRepository.count() > 0) return;

        String adminPassword = passwordEncoder.encode(dotenv.get("ADMIN_PASSWORD"));
        String managerPassword = passwordEncoder.encode(dotenv.get("MANAGER_PASSWORD"));
        String salesPassword = passwordEncoder.encode(dotenv.get("SALES_PASSWORD"));

        userRepository.save(createAdmin(adminPassword));

        List<User> managers = IntStream.rangeClosed(1, TOTAL_MANAGERS)
                .mapToObj(i -> createManager(i, managerPassword)).toList();
        userRepository.saveAll(managers);

        List<User> sales = IntStream.rangeClosed(1, TOTAL_SALES)
                .mapToObj(i -> createSales(i, salesPassword, managers))
                .toList();
        userRepository.saveAll(sales);
    }

    private User createAdmin(String password){
        User user = new User();

        user.setEmployeeCode(dotenv.get("ADMIN_CODE"));
        user.setFullName(dotenv.get("ADMIN_NAME"));
        user.setEmail(dotenv.get("ADMIN_EMAIL") + dotenv.get("EMAIL_DOMAIN"));
        user.setPhone(dotenv.get("ADMIN_PHONE"));
        user.setPassword(password);
        user.setRole(Role.ADMIN);
        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    private User createManager(int index, String password){
        User user = new User();

        user.setEmployeeCode(String.format(dotenv.get("MANAGER_CODE") + "%04d", index));
        user.setFullName(dotenv.get("MANAGER_NAME") + " " + index);
        user.setEmail(dotenv.get("MANAGER_EMAIL_NAME") + index + dotenv.get("EMAIL_DOMAIN"));
        user.setPassword(password);
        user.setPhone(String.format(dotenv.get("MANAGER_PHONE") + "%07d", index));
        user.setRole(Role.MANAGER);
        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    private User createSales(int index, String password, List<User> managers){
        User user = new User();

        user.setEmployeeCode(String.format(dotenv.get("SALES_CODE") + "%04d", index));
        user.setFullName(dotenv.get("SALES_NAME") + " " + index);
        user.setEmail(dotenv.get("SALES_EMAIL_NAME") + index + dotenv.get("EMAIL_DOMAIN"));
        user.setPhone(String.format(dotenv.get("SALES_PHONE") + "%07d", index));
        user.setPassword(password);
        user.setRole(Role.SALES);
        user.setStatus(randomStatus());
        user.setManager(managers.get(random.nextInt(managers.size())));

        return user;
    }

    private UserStatus randomStatus(){
        int value = random.nextInt(100);

        if(value < 95) return UserStatus.ACTIVE;

        if(value < 98) return UserStatus.ON_LEAVE;

        return UserStatus.RESIGNED;
    }
}

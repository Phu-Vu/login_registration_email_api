package com.example.login_registration_api.appuser;

import com.example.login_registration_api.registration.token.ConfirmationToken;
import com.example.login_registration_api.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
    private final AppRepository appRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenServicel;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser){
        boolean userExists = appRepository.findByEmail(appUser.getEmail())
                .isPresent();
//        isPresent kiểm tra đối tượng xem có rỗng hay không, nếu rỗng thì nó trả về fasle
        if (userExists){
            throw new IllegalStateException("Email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appRepository.save(appUser);

//        TODO: Tạo mã thông báo để xác nhận tài khoản

//        tạo mã token ngẫu nhiên
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenServicel.saveConfirmationToken(confirmationToken);
//        TODO:

        return token;
    }

    public int enableAppUser(String email) {
        return appRepository.enableAppUser(email);
    }
}

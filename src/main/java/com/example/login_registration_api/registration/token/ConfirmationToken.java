package com.example.login_registration_api.registration.token;

import com.example.login_registration_api.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime creatAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(String token, LocalDateTime creatAt,
                             LocalDateTime expiresAt, AppUser appUser) {
        this.token = token;
        this.creatAt = creatAt;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }
}

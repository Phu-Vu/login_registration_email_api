package com.example.login_registration_api.email;

public interface EmailSender {
    void send(String to, String email);
}

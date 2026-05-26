package com.system.animals.modules.user.service;

public interface EmailService {

    void sendCode(String email, String code);
}

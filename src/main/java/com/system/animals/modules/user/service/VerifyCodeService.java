package com.system.animals.modules.user.service;

import com.system.animals.modules.user.dto.RegisterRequest;
import com.system.animals.shared.enums.VerifyCodeResult;

public interface VerifyCodeService {


    String generateCode();
    void register(RegisterRequest request);
    void sendCode(String email);
    VerifyCodeResult verifyCode(String email, String code);
}

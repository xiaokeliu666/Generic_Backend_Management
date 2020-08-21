package com.xliu.qch.baseadmin.config.security;

import com.xliu.qch.baseadmin.util.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordConfig implements PasswordEncoder {

//    charSequence: the password that user input
//    password: the password that stored in database
    @Override
    public String encode(CharSequence charSequence) {
        return MD5Util.getMD5(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String password) {
        return password.contentEquals(encode(charSequence));
    }
}

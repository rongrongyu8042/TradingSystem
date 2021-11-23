package com.mars.counter.service;

import com.mars.counter.bean.res.Account;
import com.mars.counter.bean.res.CounterRes;
import org.springframework.stereotype.Service;

public interface IAccountService {
    public Account login(long uid, String password, String captchaByUserInput, String captchaIdBySystem);

    public void logout(String token);

    boolean updatePwd(int uid, String oldPwd, String newPwd);
}

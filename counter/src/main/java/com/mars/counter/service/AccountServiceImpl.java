package com.mars.counter.service;

import com.mars.counter.bean.res.Account;
import com.mars.counter.bean.res.CounterRes;
import com.mars.counter.cache.CacheType;
import com.mars.counter.cache.RedisStringCache;
import com.mars.counter.util.DbUtil;
import com.mars.counter.util.JsonUtil;
import com.mars.counter.util.TimeformatUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thirdpart.uuid.GudyUuid;

import java.util.Date;

import static com.mars.counter.bean.res.CounterRes.RELOGIN;
import static com.mars.counter.bean.res.CounterRes.SUCCESS;

@Service
public class AccountServiceImpl implements IAccountService {
    /**
     * 账户登录
     * @param uid
     * @param password
     * @param captchaByUserInput
     * @param captchaIdBySystem
     * @return
     */
    @Override
    public Account login(long uid, String password, String captchaByUserInput, String captchaIdBySystem) {
        //1.入参合法性校验
        if(StringUtils.isAnyBlank(password,captchaByUserInput,captchaIdBySystem)){
            return null;
        }

        //2.验证验证码是否正确
        String captchaBySystem = RedisStringCache.get(captchaIdBySystem, CacheType.ACCOUNT);
        if(StringUtils.isEmpty(captchaBySystem) || !captchaBySystem.equalsIgnoreCase(captchaByUserInput)){
            return null;
        }
        RedisStringCache.remove(captchaIdBySystem,CacheType.ACCOUNT);

        //3.验证用户名与密码
        Account account = DbUtil.queryAccount(uid,password);
        if(account==null){
            return null;
        }
        /**
         * 验证成功：
         *  1.增加唯一ID标识
         *  2.写入缓存
         *  3.更新登录时间
         */
        account.setToken(String.valueOf(GudyUuid.getInstance().getUUID()));
        RedisStringCache.cache(String.valueOf(account.getToken()), JsonUtil.toJson(account),CacheType.ACCOUNT);
        Date date  = new Date();
        DbUtil.updateLoginTime(uid,
                TimeformatUtil.yyyyMMdd(date.getTime()),
                TimeformatUtil.hhMMss(date.getTime()));
        return account;
    }


    /**
     * 退出登录
     * @param token
     */
    @Override
    public void logout(String token) {
        RedisStringCache.remove(token,CacheType.ACCOUNT);
        return;
    }

    @Override
    public boolean updatePwd(int uid, String oldPwd, String newPwd) {
        int res = DbUtil.updatePwd(uid,oldPwd,newPwd);
        return res == 0 ? false : true;
    }


}

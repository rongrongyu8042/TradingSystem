package com.mars.counter.controller;

import com.gudy.counter.util.Captcha;
import com.mars.counter.bean.res.Account;
import com.mars.counter.bean.res.CaptchaRes;
import com.mars.counter.bean.res.CounterRes;
import com.mars.counter.cache.CacheType;
import com.mars.counter.cache.RedisStringCache;
import com.mars.counter.service.IAccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import thirdpart.uuid.GudyUuid;

import java.io.IOException;

import static com.mars.counter.bean.res.CounterRes.*;

@RestController
@RequestMapping("/login")
@Log4j2
public class LoginController {

    /**
     * 生成验证码，ID为uuid，传给前端
     * @return
     * @throws IOException
     */
    @RequestMapping("/captcha")
    public CounterRes captcha() throws IOException {
        //1.生成验证码
        Captcha captcha = new Captcha(120,40,4,10);
        //2.将验证码<ID,数值>放入Redis
        String uuid = String.valueOf(GudyUuid.getInstance().getUUID());
        RedisStringCache.cache(uuid,captcha.getCode(), CacheType.CAPTCHA);
        //3.使用base64编码图片，并返回给前台
        CaptchaRes res = new CaptchaRes(uuid,captcha.getBase64ByteStr());
        return new CounterRes(res);
    }

    @Autowired
    private IAccountService accountService;

    /**
     * 1
     * @param uid
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping("/userlogin")
    public CounterRes login(long uid,String password,String captchaByUserInput,String captchaIdBySystem) throws Exception{
        Account account = accountService.login(uid, password, captchaByUserInput, captchaIdBySystem);
        if(account==null){;
            return new CounterRes(FAIL,"用户名密码/验证码错误，登录失败",null);
        }else{
            return new CounterRes(account);
        }
    }

    @RequestMapping("/loginfail")
    public CounterRes loginFail(){
        return new CounterRes(RELOGIN,"请重新登陆",null);
    }

    //退出登录
    @RequestMapping("/logout")
    public CounterRes logout(@RequestParam String token){
        accountService.logout(token);
        return new CounterRes(SUCCESS,"退出成功",null);
    }

    @RequestMapping("pwdupdate")
    public CounterRes pwdUpdate(@RequestParam int uid,
                                @RequestParam String oldpwd,
                                @RequestParam String newpwd){
        boolean res = accountService.updatePwd(uid, oldpwd, newpwd);
        if(res){
            return new CounterRes(SUCCESS,"密码更新成功",null);
        }else {
            return new CounterRes(FAIL,"密码更新失败",null);
        }

    }

}

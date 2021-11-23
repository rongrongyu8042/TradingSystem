package com.mars.counter.bean.res;

import lombok.*;

/**
 * 验证码返回值
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CaptchaRes {
    private String id;
    private String imageBase64;

    public CaptchaRes(CaptchaRes res) {

    }
}


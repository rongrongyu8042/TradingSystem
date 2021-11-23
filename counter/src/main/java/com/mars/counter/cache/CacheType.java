package com.mars.counter.cache;

public enum CacheType {
    CAPTCHA("captcha:"), //验证码
    ACCOUNT("account:"),
    ORDER("order:"),
    TRADE("trade:"),
    POSI("posi"),
    ;

    private String type;
    CacheType(String type){
        this.type = type;
    }
    public String type(){
        return this.type;
    }
}

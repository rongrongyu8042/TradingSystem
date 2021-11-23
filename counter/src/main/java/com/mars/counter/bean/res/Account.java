package com.mars.counter.bean.res;


import lombok.*;

/**
 * 账户
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Account {

    @NonNull
    private int id;

    @NonNull
    private long uid;

    @NonNull
    private String lastLoginDate;

    @NonNull
    private String lastLoginTime;

    private String token;

}

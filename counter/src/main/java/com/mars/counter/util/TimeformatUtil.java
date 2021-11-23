package com.mars.counter.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * 时间格式化Util，封装魔法数
 */
public class TimeformatUtil {

    private static final String YYYY_MM_DD = "yyyyMMdd";

    private static final String HH_MM_SS = "HH:mm:ss";

    //时间戳
    public static String yyyyMMdd(long timestamp){
        return DateFormatUtils.format(timestamp,YYYY_MM_DD);
    }
    public static String hhMMss(long timestamp){
        return DateFormatUtils.format(timestamp,HH_MM_SS);
    }

}

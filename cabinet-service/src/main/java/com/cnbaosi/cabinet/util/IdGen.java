package com.cnbaosi.cabinet.util;

import java.util.UUID;

/**
 * ID工具类
 *
 * @author Yifeng Wang
 */
public class IdGen {

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

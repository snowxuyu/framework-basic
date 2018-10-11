package org.framework.common.util;

import java.util.Random;

/**
 * Copyright @ 2017QIANLONG.
 * All right reserved.
 * Class Name : org.framework.common.util
 * Description : framework-basic
 * Author : snowxuyu
 * Date : 2017/3/20
 */

public abstract class RandomUtils {

    /**
     * 生成指定长度的随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+=";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}

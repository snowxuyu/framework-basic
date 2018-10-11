package org.framework.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Copyright @ 2016QIANLONG.
 * All right reserved.
 * Class Name : org.framework.common.util
 * Description :
 * Author : snowxuyu
 * Date : 2016/11/29
 */

public abstract class BillNoUtils {

    /**
     * 生成订单号：五位随机数+当前年月日时分秒毫秒
     *
     * @return
     */
    public static String generateBillNo() {

        Date date = Calendar.getInstance().getTime();

        String str = DateUtils.convert(date, DateUtils.DATE_TIMESTAMP_SHORT_FORMAT);

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数

        return str + rannum ;
    }
}

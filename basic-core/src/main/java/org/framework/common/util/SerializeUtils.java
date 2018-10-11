package org.framework.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Copyright @ 2017QIANLONG.
 * All right reserved.
 * Class Name : org.framework.common.util
 * Description : framework-basic
 * Author : snowxuyu
 * Date : 2017/2/16
 */

public abstract class SerializeUtils {
    public static final Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            //序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            logger.error("SerializeUtil Error!",e);
        }
        return null;
    }

    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        if(bytes==null){
            return null;
        }
        try {
            //反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error("UnserializeUtil Error!",e);
        }
        return null;
    }
}


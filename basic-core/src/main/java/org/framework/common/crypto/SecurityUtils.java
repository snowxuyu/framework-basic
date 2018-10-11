package org.framework.common.crypto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Copyright @ 2016QIANLONG.
 * All right reserved.
 * Class Name : org.framework.common.crypto
 * Description :
 * Author : snowxuyu
 * Date : 2016/11/28
 */

public abstract class SecurityUtils {

    private final static String INPUT_CHARSET = "UTF-8";//参数编码字符集

    /**
     * MD5 安全加密算法
     *
     * @param content 加密内容
     * @return
     */
    public static String md5(String content) {
        return md5(content, INPUT_CHARSET);
    }

    /**
     * MD5 安全加密算法
     *
     * @param content 加密内容
     * @param charset 字符集
     * @return
     */
    public static String md5(String content, String charset) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest digest = SecurityUtils.getDigest("MD5");
            // 使用指定的字节更新摘要
            digest.update(content.getBytes(charset));
            // 获得密文
            byte[] messageDigest = digest.digest();
            // 把密文转换成十六进制的字符串形式
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码集错误！指定编码为:" + charset);
        }
    }


    /**
     * SHA1 安全加密算法
     *
     * @param content 加密内容
     * @return
     */
    public static String sha1(String content) {
        return sha1(content, INPUT_CHARSET);
    }

    /**
     * SHA1 安全加密算法
     *
     * @param content 加密内容
     * @param charset 字符集
     * @return
     */
    public static String sha1(String content, String charset) {
        try {
            //指定sha1算法
            MessageDigest digest = SecurityUtils.getDigest("SHA-1");
            digest.update(content.getBytes(charset));
            //获取字节数组
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码集错误！指定编码为:" + charset);
        }
    }


    /**
     * SHA256 安全加密算法
     *
     * @param content 加密内容
     * @return
     */
    public static String sha256(String content) {
        return sha256(content, INPUT_CHARSET);
    }

    /**
     * SHA256 安全加密算法
     *
     * @param content 加密内容
     * @param charset 字符集
     * @return
     */
    public static String sha256(String content, String charset) {
        try {
            //指定sha256算法
            MessageDigest digest = SecurityUtils.getDigest("SHA-256");
            digest.update(content.getBytes(charset));
            //获取字节数组
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("字符编码集错误！指定编码为:" + charset);
        }
    }


    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
        }
    }
}

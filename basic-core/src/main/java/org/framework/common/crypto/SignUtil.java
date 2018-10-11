package org.framework.common.crypto;

/**
 * 签名工具类: 加签、验签
 */
public abstract class SignUtil {

    private final static String INPUT_CHARSET = "UTF-8";//参数编码字符集

    /** MD5方式签名 */
    public final static String MD5_SIGN = "MD5";

    /** SHA-1方式签名 */
    public final static String SHA1_SIGN = "SHA-1";

    /** SHA-256方式签名 */
    public final static String SHA256_SIGN = "SHA-256";


    /**
     * 签名
     *
     * @param content  签名内容
     * @param signType 签名方式
     * @param signKey  签名密钥
     * @return
     * @throws Exception
     */
    public static String sign(String content, String signType, String signKey) throws Exception {
        return sign(content, signType, signKey, INPUT_CHARSET);
    }

    /**
     * 签名算法
     *
     * @param content  签名内容
     * @param signType 签名方式
     * @param signKey  签名密钥
     * @param charset  字符集
     * @return
     * @throws Exception
     */
    public static String sign(String content, String signType, String signKey, String charset) throws Exception {
        String signContent = "";
        content = content + signKey;
        if (MD5_SIGN.equalsIgnoreCase(signType)) {
            signContent = SecurityUtils.md5(content, charset);
        } else if (SHA1_SIGN.equalsIgnoreCase(signType)) {
            signContent = SecurityUtils.sha1(content, charset);
        } else if (SHA256_SIGN.equalsIgnoreCase(signType)) {
            signContent = SecurityUtils.sha256(content, charset);
        }
        return signContent;
    }

    /**
     * 签名验证
     *
     * @param content     原始内容
     * @param signContent 签名内容
     * @param signKey     签名密钥
     * @return
     * @throws Exception
     */
    public static boolean verify(String content, String signContent, String signKey) throws Exception {
        return verify(content, signContent, signKey, INPUT_CHARSET);
    }

    /**
     * 签名验证
     *
     * @param content     原始内容
     * @param signContent 签名内容
     * @param signKey     签名密钥
     * @param charset     字符集
     * @return
     * @throws Exception
     */
    public static boolean verify(String content, String signContent, String signKey, String charset)
            throws Exception {
        String targetContent = sign(content, signKey, charset);
        return targetContent.equals(signContent);
    }
}

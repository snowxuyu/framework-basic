package org.framework.common.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * 对称加解
 * <p>
 * 默认DESede
 * <p>
 * 支持 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR)
 * <p>
 * DES                key size must be equal to 56
 * DESede(TripleDES)  key size must be equal to 112 or 168
 * AES                key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
 * Blowfish           key size must be multiple of 8, and can only range from 32 to 448 (inclusive) RC2 key size must be between 40 and 1024 bits
 * RC4(ARCFOUR)       key size must be between 40 and 1024 bits
 */
public abstract class DESUtils {

    private static String algorithm = "DESede"; //算法

    public static void setAlgorithm(String algorithm) {
        DESUtils.algorithm = algorithm;
    }

    /**
     * 密钥转换 默认DES
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        return toKey(key, algorithm);
    }

    /**
     * 转换密钥<br>
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key, String algorithm) throws Exception {
    	SecretKey secretKey = null;
		if (algorithm.equalsIgnoreCase("DES")) {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			secretKey = keyFactory.generateSecret(dks);
		} else if ( algorithm.equalsIgnoreCase("DESede") ) {
            DESedeKeySpec dks = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            secretKey = keyFactory.generateSecret(dks);
        } else {
			secretKey = new SecretKeySpec(key, algorithm);
		}
		return secretKey;
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] data, String key) throws Exception {
        Key k = toKey(Base64Utils.decode(key));

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, k);

        return Base64Utils.encode(cipher.doFinal(data));
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(byte[] data, String key) throws Exception {
        Key k = toKey(Base64Utils.decode(key));
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, k);

        return Base64Utils.encode(cipher.doFinal(data));
    }


    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        Key k = toKey(Base64Utils.decode(key));

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, k);

        return new String(cipher.doFinal(Base64Utils.decode(data)));
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        Key k = toKey(Base64Utils.decode(key));
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, k);

        return Base64Utils.encode(cipher.doFinal(data.getBytes("UTF-8")));
    }

    /**
     * 生成密钥
     *
     * @return
     * @throws Exception
     */
    public static String initKey() throws Exception {
        return initKey(algorithm);
    }

    /**
     * 生成密钥
     *
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static String initKey(String algorithm) throws Exception {

        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        setAlgorithm(algorithm);

        SecureRandom secureRandom = new SecureRandom();
        kg.init(secureRandom);

        SecretKey secretKey = kg.generateKey();

        return Base64Utils.encode(secretKey.getEncoded());
    }
}

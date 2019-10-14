package xl.test.encryption;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;

public class DESUtil {
	/**
	 * 加密 
	 *@create 2013-3-22  
	 * @param plainText	没有加密的文本
	 * @param secretKey	密钥
	 * @return
	 */
	public final static String encrypt(String plainText, String secretKey) {
		try {
			return byte2hex(encrypt(plainText.getBytes("UTF-8"), secretKey.getBytes()));
		} catch (Exception e) {
		}
		return null;
	}
	private final static String ALGORITHM = "DES";	
	/**
	 * 加密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return	  返回加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		return cipher.doFinal(src);
	}
	
	/**
	 * 解密
	 *@author jim
	 *@create 2013-3-22
	 * @param cipherText	密文
	 * @param secretKey	密钥
	 * @return
	 */
	public final static String decrypt(byte[] cipherText, String secretKey) {
        try {
            return new String(decrypt(hex2byte(cipherText), secretKey.getBytes()),"UTF-8");
        } catch (Exception e) {
        }
        return null;
    }
	
	/**
	 * 解密
	 * @param src	数据源
	 * @param key	密钥，长度必须是8的倍数
	 * @return		返回解密后的原始数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		return cipher.doFinal(src);
	}
	
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
	
	/**
	 * 二行制转字符串
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	
	/**
     * Description 根据键值进行加密
     * @param data 
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encryptBase64(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
        String strs = Base64.encodeBase64String(bt);
        return strs;
    }
 
    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decryptBase64(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        byte[] buf = Base64.decodeBase64(data);
        byte[] bt = decrypt(buf,key.getBytes());
        return new String(bt,"UTF-8");
    }
}

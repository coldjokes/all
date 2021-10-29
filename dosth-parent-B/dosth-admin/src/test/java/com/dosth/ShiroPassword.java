package com.dosth;

import java.security.Key;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Test;

import com.dosth.common.util.AESUtil;

public class ShiroPassword {
	
    private static final String str = "111111";
    private static final String salt = "3b9j6";
	public final static int hashIterations = 1024;
    
    private static final String end = "bbc5b1d5ecbacde6b2075aa8344a1b8e";
    
    @Test
    public void passWord1() {
        String base64Encoded = Base64.encodeToString(str.getBytes());
        String str2 = Base64.decodeToString(base64Encoded);
        System.out.println("base64Encoded====" + base64Encoded);
        System.out.println("str2====" + str2);
        System.out.println(str.equals(str2));
        System.out.println(end);
    }
    @Test
    public void passWord2() {
        String base64Encoded = Hex.encodeToString(str.getBytes());
        String str2 = new String(Hex.decode(base64Encoded.getBytes()));
        System.out.println("base64Encoded=" + base64Encoded);
        System.out.println("str2=" + str2);
        System.out.println(end);
    }

    @Test
    public void passWord3() {
        String md5 = new Md5Hash(str).toString();// 还可以转换为 toBase64()/toHex()
        System.out.println("md5===" + md5);
        String md5AndSqltt = new Md5Hash(str, salt).toString();// 还可以转换为
                                                                // toBase64()/toHex()
        System.out.println("md5AndSqltt===" + md5AndSqltt);
        System.out.println(end);
    }
    @Test
    public void passWord4() {
		ByteSource salt1 = new Md5Hash(salt);
        String sha1 = new Md5Hash(str, salt1, hashIterations).toString();
        System.out.println(sha1);
        System.out.println(end);
    }
    
    @Test
    public void md5() {
		ByteSource salt1 = new Md5Hash(salt);
		String result = new SimpleHash("MD5", str, salt1, hashIterations).toString();
		System.out.println(result);
        System.out.println(end);
	}

    @Test
    public void passWord5() {
        // 内部使用MessageDigest
        String simpleHash = new SimpleHash("MD5", str, salt).toString();
        System.out.println("simpleHash=" + simpleHash);
        System.out.println(end);
    }

    @Test
    public void passWord6() {
        DefaultHashService hashService = new DefaultHashService(); // 默认算法SHA-512
        hashService.setHashAlgorithmName("MD5");
        hashService.setPrivateSalt(new SimpleByteSource(salt)); // 私盐，默认无
        hashService.setGeneratePublicSalt(true);// 是否生成公盐，默认false
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());// 用于生成公盐。默认就这个
        hashService.setHashIterations(hashIterations); // 生成Hash值的迭代次数
        HashRequest request = new HashRequest.Builder().setAlgorithmName("MD5")
                .setSource(ByteSource.Util.bytes(str))
                .setSalt(ByteSource.Util.bytes(salt)).setIterations(hashIterations).build();
        String hex = hashService.computeHash(request).toHex();
        System.out.println("hex=" + hex);
        System.out.println(end);
    }
    // AES算法实现
    @Test
    public void passWord7() {
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128); // 设置key长度
        // 生成key
        Key key = aesCipherService.generateNewKey();
        // 加密
        String encrptText = aesCipherService
                .encrypt(str.getBytes(), key.getEncoded()).toHex();
        // 解密
        String text2 = new String(aesCipherService
                .decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

        System.out.println("text2=" + text2);
        System.out.println("encrptText=" + encrptText);
        System.out.println(end);
    }

    @Test
    public void passWord8() {
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128); // 设置key长度
        // 生成key
        Key key = aesCipherService.generateNewKey();
        // 加密
        String encrptText = aesCipherService
                .encrypt(str.getBytes(), key.getEncoded()).toHex();
        // 解密
        String text2 = new String(aesCipherService
                .decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

        System.out.println("text2=" + text2);
        System.out.println("encrptText=" + encrptText);
        System.out.println(end);
    }
    
    /** 
     * base64加密 
     * @param str 
     * @return 
     */  
    public static String encBase64(String str){  
        return Base64.encodeToString(str.getBytes());  
    }  
      
    /** 
     * base64解密 
     * @param str 
     * @return 
     */  
    public static String decBase64(String str){  
        return Base64.decodeToString(str);  
    } 
    
    @Test
    public void password9() {
    	String password = ShiroPassword.encBase64("123");
    	System.out.println(password);
    	System.out.println(ShiroPassword.encBase64("1234"+salt));
    	System.out.println(ShiroPassword.encBase64("12345"+salt));
    	System.out.println(ShiroPassword.encBase64("123456"+salt));
    	System.out.println(ShiroPassword.encBase64("1234567"+salt));
    	System.out.println(ShiroPassword.encBase64("12345678"+salt));
    	System.out.println(ShiroPassword.decBase64(password));
    }
    
    @Test
    public void passwordA() {
    	String password = AESUtil.aesEncode(str);
    	System.out.println(password);
    	System.out.println(AESUtil.aesEncode("1234"+salt));
    	System.out.println(AESUtil.aesEncode("12345"+salt));
    	System.out.println(AESUtil.aesEncode("123456"+salt));
    	System.out.println(AESUtil.aesEncode("1234567"+salt));
    	System.out.println(AESUtil.aesEncode("12345678"+salt));
    	System.out.println(AESUtil.aesDecode(password));
    	

    	System.out.println(AESUtil.aesDecode("MCtV/dPQ33ivSg6XjttKXQ=="));
    }
}
/**
 * 
 */
package com.apoollo.auto.connect.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author liuyulong
 * @since 2025-04-09
 */
public class EncryptionUtils {

	public static String md5ToUpperHexString(String input) {
		return md5ToHexString(input).toUpperCase();
	}
	
	public static String md5ToHexString(String input) {
		return ByteArrayUtils.toHexString(md5(input.getBytes(StandardCharsets.UTF_8)));
	}

	public static byte[] md5(byte[] bs) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bs);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] encrypt(byte[] key, byte[] iv, byte[] input) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			IvParameterSpec params = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
			return cipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] decrypt(byte[] key, byte[] iv, byte[] input) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			IvParameterSpec params = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
			return cipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

}

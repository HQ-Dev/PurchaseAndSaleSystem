package com.archy.test.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MD5 {

	private static String result;
	
	public static String jkdMD5(String sourceCode) {
		// 得到一个MD5算法的加密对象
		try {
			MessageDigest md =  MessageDigest.getInstance("MD5");
			byte[] md5Bytes = md.digest(sourceCode.getBytes());
			result =  md5Bytes.toString();
//			// 需要转成16进制,可以借助第三方的包
//			System.out.println("JDK MD5: " + Hex.encodeHexString(md5Bytes));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}

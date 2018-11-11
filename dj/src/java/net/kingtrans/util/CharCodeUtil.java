package net.kingtrans.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 字符编码工具类
 * 
 * @author Cheng
 */
public class CharCodeUtil {

	/**
	 * @Description MD5编码
	 */
	public static String MD5(String s) {
		return DigestUtils.md5Hex(s);
	}

	/**
	 * @Description sha256 编码
	 */
	public static String sha256(String s) {
		return DigestUtils.sha256Hex(s);
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	public static String sha256_HMAC(String message, String secret) {
		String hash = "";
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] data = sha256_HMAC.doFinal(message.getBytes());
			// byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
			// hash = bytes2Hex(bytes);
			char[] toDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			int l = data.length;
			char[] out = new char[l << 1];
			// two characters form the hex value.
			for (int i = 0, j = 0; i < l; i++) {
				out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
				out[j++] = toDigits[0x0F & data[i]];
			}
			for (char c : out) {
				hash += c;
			}
			return hash;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hash;
	}

	public static String encode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static String decode(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	// 转换字符为utf-8，不能转换的使用“？”替代
	public static String getUTF8Str(String str) {
		String encode = "UTF-8";
		StringBuffer result = new StringBuffer("");
		try {
			for (int i = 0; i < str.length(); i++) {
				String c = "" + str.charAt(i);
				if (c.equals(new String(c.getBytes(encode), encode))) {
					result.append(c);
				} else {
					result.append("?");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return result.toString();
	}

	public static String replaceSplit(String string) {
		if (string == null || string.trim().length() == 0) {
			return "";
		}
		string = string.replaceAll("，", ";").replaceAll("\\.", ";").replaceAll("、", ";").replaceAll(" ", ";")
				.replaceAll("；", ";").replaceAll("。", ";").replaceAll(",", ";").replaceAll("/", ";")
				.replaceAll("	", ";");
		return string.trim();
	}

}

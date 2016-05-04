package com.mamba.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.ECPoint;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sansec.asn1.pkcs.GBObjectIdentifiers;
import com.sansec.asn1.pkcs.PKCSObjectIdentifiers;
import com.sansec.asn1.pkcs.SM2PublicKeyStructure;
import com.sansec.asn1.x509.AlgorithmIdentifier;
import com.sansec.asn1.x509.SubjectPublicKeyInfo;
import com.sansec.jce.provider.JCESM2PublicKey;
import com.sansec.jce.provider.SwxaProvider;
import com.sansec.util.BigIntegers;

import sun.security.rsa.RSAPublicKeyImpl;

public class DannyUtils {

	/**
	 * 检查邮箱地址格式
	 * 
	 * @param mailAddress
	 * @return
	 */
	public static boolean checkMailAddress(String mailAddress) {
		Pattern pattern = Pattern.compile(
				"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		return pattern.matcher(mailAddress).matches();
	}

	/**
	 * 向邮箱mailAdress发送私钥备份文件
	 * 
	 * @param mailAddress
	 * @throws MessagingException
	 */
	public static void mailSender(String mailAddress, String filePath) throws MessagingException {
		String mailSenderAddress = "secsmarts";
		String mailSenderPassword = "secsmarts2016";

		Properties properties = new Properties();
		properties.setProperty("mail.host", "smtp.sina.com");
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "true");

		Session session = Session.getInstance(properties);
		Transport transport = session.getTransport();
		transport.connect("smtp.sina.com", mailSenderAddress, mailSenderPassword);
		// 设置邮件内容
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("secsmarts@sina.com"));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
		// 邮件标题
		String mailSubject = "license服务器私钥备份";
		message.setSubject(mailSubject);
		// 邮件正文
		MimeBodyPart mailContent = new MimeBodyPart();
		String mailText = "您好，请保存附件中的私钥备份文件. ";
		mailContent.setContent(mailText, "text/html;charset=UTF-8");
		// 邮件附件
		MimeBodyPart mailAttach = new MimeBodyPart();
		DataHandler dataHandler = new DataHandler(new FileDataSource(filePath));
		mailAttach.setDataHandler(dataHandler);
		mailAttach.setFileName(dataHandler.getName());
		MimeMultipart mimeMultipart = new MimeMultipart();
		mimeMultipart.addBodyPart(mailContent);
		mimeMultipart.addBodyPart(mailAttach);
		mimeMultipart.setSubType("mixed");

		message.setContent(mimeMultipart);
		message.saveChanges();

		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
		System.out.println("私钥备份已发送至邮箱" + mailAddress);

	}

	/**
	 * 检验管理员自定义口令的强度是否符合要求，若符合要求则返回true
	 * 
	 * @param pw
	 * @return
	 */
	public static boolean checkAdminPassword(String pw) {
		boolean flag = false;
		if (pw.length() < 16) {
			return flag;
		} else {
			boolean flag_a2z = false;
			boolean flag_A2Z = false;
			boolean flag_symbol = false;
			boolean flag_number = false;

			char[] a2z = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
					's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
			char[] A2Z = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
					'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
			char[] symbols = { ',', '.', '/', '~', '!', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '_', '<',
					'>', '?' };
			char[] numbers = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
			for (int i = 0; i < a2z.length; i++) {
				if (pw.indexOf(a2z[i]) >= 0) {
					flag_a2z = true;
					break;
				}
			}
			for (int i = 0; i < A2Z.length; i++) {
				if (pw.indexOf(A2Z[i]) >= 0) {
					flag_A2Z = true;
					break;
				}
			}
			for (int i = 0; i < symbols.length; i++) {
				if (pw.indexOf(symbols[i]) >= 0) {
					flag_symbol = true;
					break;
				}
			}
			for (int i = 0; i < numbers.length; i++) {
				if (pw.indexOf(numbers[i]) >= 0) {
					flag_number = true;
					break;
				}
			}
			flag = flag_a2z & flag_A2Z & flag_symbol & flag_number;
			return flag;
		}
	}

	/**
	 * serialize an object to a byte array
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] serializeObject(Object obj) throws IOException {
		byte[] bs = null;
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(obj);
		bs = bo.toByteArray();
		bo.close();
		oo.close();
		return bs;
	}

	/**
	 * deserialize a byte array back to an object
	 * 
	 * @param bs
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserializeObject(byte[] bs) throws IOException, ClassNotFoundException {
		Object obj = null;
		ByteArrayInputStream bi = new ByteArrayInputStream(bs);
		ObjectInputStream oi = new ObjectInputStream(bi);
		obj = oi.readObject();
		bi.close();
		oi.close();
		return obj;
	}

	/**
	 * Decrypt byte array with AES/CBC; IV is "0000000000000000"
	 * 
	 * @param key
	 * @param encrypted
	 * @return
	 */
	public static byte[] decryptAESCBC(Key key, byte[] encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(new byte[16]);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);

			byte[] original = cipher.doFinal(encrypted);
			return original;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static byte[] encryptAESCBC(byte[] data, Key key) {
		try {
			IvParameterSpec iv = new IvParameterSpec(new byte[16]);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] encrypted = cipher.doFinal(data);
			return encrypted;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * encrypt byte array using RSA
	 * 
	 * @param text
	 * @param publicKey
	 * @return
	 */
	public static byte[] encryptRSA(byte[] text, PublicKey publicKey) {
		byte[] cipherBytes = null;
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			cipherBytes = cipher.doFinal(text);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return cipherBytes;
	}

	/**
	 * decrypt byte array using RSA
	 * 
	 * @param text
	 * @param privateKey
	 * @return
	 */
	public static byte[] decryptRSA(byte[] text, PrivateKey privateKey) {
		byte[] originalBytes = null;
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			originalBytes = cipher.doFinal(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return originalBytes;
	}

	/**
	 * Convert PublicKey object to String
	 * 
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String rsaPuk2String(PublicKey publicKey) throws Exception {
		RSAPublicKeyImpl puk = new RSAPublicKeyImpl(publicKey.getEncoded());
		String strModulus = puk.getModulus().toString(16);// 256B||512B
		return strModulus;
	}

	/**
	 * Convert PrivateKey Object to String
	 * 
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String rsaPrk2String1024(PrivateKey privateKey) throws Exception {
		RSAPrivateCrtKey pri1 = (RSAPrivateCrtKey) privateKey;
		String d_strModulus = pri1.getModulus().toString(16);
		String p_strPrimeP = pri1.getPrimeP().toString(16);
		String q_strPrimeQ = pri1.getPrimeQ().toString(16);
		return d_strModulus + p_strPrimeP + q_strPrimeQ;// 512B
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static byte[] oR(byte[] r1, byte[] r2) {
		byte[] res = new byte[r1.length];
		for (int i = 0; i < r1.length; ++i) {
			res[i] = (byte) (r1[i] ^ r2[i]);
		}
		return res;
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/*
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src byte[] data
	 * 
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * convert the publicKey from string format to byte[]
	 * 
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static PublicKey makePublicKey(String publicKey) throws Exception {
		Security.addProvider(new SwxaProvider());
		byte[] pubKey = string2Bytes(publicKey);
		BigInteger x = new BigInteger(1, Arrays.copyOfRange(pubKey, 0, 32));
		BigInteger y = new BigInteger(1, Arrays.copyOfRange(pubKey, 32, 64));
		SM2PublicKeyStructure pubStruc = new SM2PublicKeyStructure(new ECPoint(x, y));
		SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(
				new AlgorithmIdentifier(PKCSObjectIdentifiers.ecPublicKey, GBObjectIdentifiers.sm2),
				pubStruc.getPublicKey());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(info.getDEREncoded());
		KeyFactory keyFactory = KeyFactory.getInstance("sm2", "SwxaJCE");
		PublicKey publickey = keyFactory.generatePublic(keySpec);
		return publickey;
	}

	/**
	 * convert the mac from the String format to bytes retrurn the maclist
	 * 
	 * @param startMacString
	 * @param endMacString
	 * @return
	 */
	public static ArrayList<byte[]> getMacArray(String startMacString, long num) {
		ArrayList<byte[]> res = new ArrayList<byte[]>();
		if (startMacString.length() == 15) {
			long longImei = Long.parseLong(startMacString);
			while (num > 0) {
				String strTmp = Long.toString(longImei);
				int n = strTmp.length();
				for (int i = 0; i < 15 - n; i++) {
					strTmp = "0" + strTmp;
				}
				res.add(strTmp.getBytes());
				longImei++;
				num--;
			}
			return res;// 15B
		}

		if (startMacString.length() == 17) {
			long startMac = macString2Long(startMacString);
			long n = num;
			byte[] macBytes;
			for (int i = 0; i < n; ++i) {
				macBytes = new byte[6];
				long mac = startMac + (long) i;
				for (int pos = 0; pos < 6; ++pos) {
					macBytes[pos] = (byte) (mac >>> (5 - pos) * 8 & 0xFF);
				}
				res.add(i, macBytes);
			}
			return res;// 6B
		}

		return null;
	}

	/**
	 * convert the mac from String to long
	 * 
	 * @param str
	 * @return
	 */
	public static long macString2Long(String str) {
		if (str.length() == 17) {
			String[] strs = str.split(":");
			String macString = "";
			for (String string : strs) {
				macString += string;
			}
			long res = Long.valueOf(macString, 16);
			// System.out.println(Long.toHexString(res));
			return res;
		} else {
			return Long.parseLong(str);
		}
	}

	/**
	 * convert the mac from Long to String
	 * 
	 * @param str
	 * @return
	 */
	public static String macLongToString(Long mac) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			String s = Long.toHexString((mac >> (5 - i) * 8) & 0xff);
			if (s.length() < 2) {
				stringBuilder.append("0");
			}
			if (i < 5) {
				stringBuilder.append(s + ":");
				continue;
			}
			stringBuilder.append(s);
		}
		return stringBuilder.toString();
	}

	/**
	 * convert the mac from byte[] to String the format like
	 * this:0e:34:f4:23:a6:b7
	 * 
	 * @param macbytes
	 * @return
	 */
	public static String macBytesToString(byte[] macbytes) {
		String res = "";
		for (byte b : macbytes) {
			if ((b & 0xF0) == 0) {
				res += '0';
			}
			res += Integer.toHexString(b & 0xFF) + ":";
		}
		return res.substring(0, res.length() - 1);
	}

	/**
	 * string to bytes
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] string2Bytes(String s) {
		byte[] res = new byte[s.length()];
		BigInteger i = new BigInteger(s, 16);
		res = BigIntegers.asUnsignedByteArray(i);
		// System.out.println(bytes2String(res));
		return res;
	}

	/**
	 * convert to String from bytes
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2String(byte[] bytes) {
		String res = "";
		for (byte b : bytes) {
			if ((b & 0xF0) == 0) {
				res += '0';
			}
			res += Integer.toHexString(b & 0xFF);
		}
		return res;
	}

	/**
	 * print the byte[] to c pattern char[]
	 */
	public static void printBytes(String valueName, byte[] bytes) {
		System.out.println(valueName + ": " + DannyUtils.bytes2String(bytes));
		System.out.println("char " + valueName + "[] = ");
		System.out.println("{");
		System.out.print("  ");
		int i = 1;
		String s = "";
		for (byte b : bytes) {
			if ((b & 0xf0) == 0) {
				s += "0";
			}
			if (i == bytes.length) {
				System.out.print("0x" + s + Integer.toHexString(b & 0xff));
			} else
				System.out.print("0x" + s + Integer.toHexString(b & 0xff) + ", ");
			s = "";
			if (i == bytes.length) {

			}
			if (i % 16 == 0 && i != bytes.length) {
				System.out.println();
				System.out.print("  ");
			}
			if (i == bytes.length) {
				System.out.println();
			}
			i++;
		}
		System.out.println("}");
	}

	public static String printBytes2String(byte[] bytes) {
		// System.out.println(valueName + ": " + Util.bytes2String(bytes));
		// System.out.println("char " + valueName + "[] = ");
		// System.out.println("{");
		// System.out.print(" ");
		int i = 1;
		String tmp = "";
		String s = "";
		for (byte b : bytes) {
			if ((b & 0xf0) == 0) {
				s += "0";
			}
			if (i == bytes.length) {
				tmp += ("0x" + s + Integer.toHexString(b & 0xff));
			} else
				tmp += ("0x" + s + Integer.toHexString(b & 0xff) + ", ");
			s = "";
			if (i == bytes.length) {

			}
			if (i % 16 == 0 && i != bytes.length) {
				tmp += "\n";
				tmp += ("  ");
			}
			if (i == bytes.length) {
				tmp += "\n";
			}
			i++;
		}
		return tmp;
	}

	/**
	 * convert the publickey to hex string format
	 * 
	 * @param pubKey
	 * @return
	 */
	public static String publickKey2HexString(PublicKey pubKey) {
		String res = "";
		JCESM2PublicKey publicKey = (JCESM2PublicKey) pubKey;
		byte[] xBytes = BigIntegers.asUnsignedByteArray(publicKey.getW().getAffineX());
		byte[] yBytes = BigIntegers.asUnsignedByteArray(publicKey.getW().getAffineY());
		res = bytes2String(xBytes) + bytes2String(yBytes);
		// System.out.println("x: " + bytes2String(xBytes));
		// System.out.println("y: " + bytes2String(yBytes));
		return res;
	}

	public static void writeBytes(BufferedOutputStream out, byte[] bytes, String name) throws IOException {
		out.write((name + ": " + DannyUtils.bytes2String(bytes) + "\n").getBytes());
		out.write(("char " + name + "[] = \n{\n").getBytes());
		String s = "  ";
		int i = 1;
		for (byte b : bytes) {
			if ((b & 0xf0) == 0) {
				s += "0x0";
			} else {
				s += "0x";
			}
			if (i == bytes.length) {
				s += Integer.toHexString(b & 0xff);
			} else {
				s += Integer.toHexString(b & 0xff) + ", ";
			}
			if (i == bytes.length || (i) % 16 == 0) {
				out.write((s + "\n").getBytes());
				s = "  ";
			}
			++i;
		}
		out.write("}\n".getBytes());
	}

	public static byte[] mergeArray(byte[] bytes1, byte[] bytes2) {
		byte[] res = new byte[bytes1.length + bytes2.length];
		System.arraycopy(bytes1, 0, res, 0, bytes1.length);
		System.arraycopy(bytes2, 0, res, bytes1.length, bytes2.length);
		return res;
	}

	public static byte[] mergeArray(byte[] mac, byte[] random, byte[] license, byte[] privateKey, byte[] publicKey,
			byte[] sessionKey) {
		int length = mac.length + random.length + license.length + privateKey.length + publicKey.length
				+ sessionKey.length;
		byte[] res = new byte[length];
		int pos = 0;
		System.arraycopy(mac, 0, res, pos, mac.length);
		pos += mac.length;
		System.arraycopy(random, 0, res, pos, random.length);
		pos += random.length;
		System.arraycopy(license, 0, res, pos, license.length);
		pos += license.length;
		System.arraycopy(privateKey, 0, res, pos, privateKey.length);
		pos += privateKey.length;
		System.arraycopy(publicKey, 0, res, pos, publicKey.length);
		pos += publicKey.length;
		System.arraycopy(sessionKey, 0, res, pos, sessionKey.length);
		return res;
	}

}

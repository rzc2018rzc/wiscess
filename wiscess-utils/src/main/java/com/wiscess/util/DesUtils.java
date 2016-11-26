package com.wiscess.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

/**
 * DES���ܺͽ��ܹ���,���Զ��ַ������м��ܺͽ��ܲ��� ��
 */
public class DesUtils {

	private static String strDefaultKey = "cysy_cms.20151024";// �ַ���Ĭ�ϼ�ֵ
	private static enum Method {decrypt,encrypt};

	/**
	 * ��byte����ת��Ϊ��ʾ16����ֵ���ַ����� �磺byte[]{8,18}ת��Ϊ��0813�� ��public static byte[]
	 * hexStr2ByteArray(String strIn) ��Ϊ�����ת������
	 *
	 * @param arrB
	 *            ��Ҫת����byte����
	 * @return ת������ַ���
	 */
	private static String byteArray2HexStr(byte[] arrB) {
		int iLen = arrB.length;
		StringBuffer sb = new StringBuffer(iLen * 2);// ÿ��byte�������ַ����ܱ�ʾ�������ַ����ĳ��������鳤�ȵ�����
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			while (intTmp < 0) {// �Ѹ���ת��Ϊ����
				intTmp = intTmp + 256;
			}
			if (intTmp < 16) {// С��0F������Ҫ��ǰ�油0
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * ����ʾ16����ֵ���ַ���ת��Ϊbyte���飬 ��public static String byteArray2HexStr(byte[] arrB)
	 * ��Ϊ�����ת������
	 *
	 * @param strIn
	 *            ��Ҫת�����ַ���
	 * @return ת�����byte����
	 */
	private static byte[] hexStr2ByteArray(String strIn) {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		byte[] arrOut = new byte[iLen / 2];// �����ַ���ʾһ���ֽڣ������ֽ����鳤�����ַ������ȳ���2
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	private static Cipher encryptCipher(String key) throws InvalidKeyException, Exception{
		Cipher encryptCipher= Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, getKey(key.getBytes()));
		return encryptCipher;
	}
	private static Cipher decryptCipher(String key) throws InvalidKeyException, Exception{
		Cipher decryptCipher= Cipher.getInstance("DES");
		decryptCipher.init(Cipher.DECRYPT_MODE, getKey(key.getBytes()));
		return decryptCipher;
	}
	/**
	 * �����ֽ�����
	 *
	 * @param arrB
	 *            ����ܵ��ֽ�����
	 * @return ���ܺ���ֽ�����
	 * @throws Exception
	 */
	private static byte[] encryptByte(byte[] arrB) {
		return encryptByte(arrB,strDefaultKey);
	}
	private static byte[] encryptByte(byte[] arrB,String key) {
		byte[] encrypt = null;
		try {
			encrypt = encryptCipher(key).doFinal(arrB);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypt;
	}

	/**
	 * �����ַ���
	 *
	 * @param strIn
	 *            ����ܵ��ַ���
	 * @return ���ܺ���ַ���
	 * @throws Exception
	 */
	public static String encrypt(String strIn) {
		return byteArray2HexStr(encryptByte(strIn.getBytes()));
	}
	public static String encrypt(String strIn,String key) {
		return byteArray2HexStr(encryptByte(strIn.getBytes(),key));
	}

	/**
	 * �����ֽ�����
	 *
	 * @param arrB
	 *            ����ܵ��ֽ�����
	 * @return ���ܺ���ֽ�����
	 * @throws Exception
	 */
	private final static byte[] decryptByte(byte[] arrB) {
		return decryptByte(arrB,strDefaultKey);
	}
	private final static byte[] decryptByte(byte[] arrB,String key) {
		byte[] decrypt = null;
		try {
			decrypt = decryptCipher(key).doFinal(arrB);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypt;
	}

	/**
	 * �����ַ���
	 *
	 * @param strIn
	 *            ����ܵ��ַ���
	 * @return ���ܺ���ַ���
	 * @throws Exception
	 */
	public static String decrypt(String strIn) {
		return new String(decryptByte(hexStr2ByteArray(strIn)));
	}
	public static String decrypt(String strIn,String key) {
		return new String(decryptByte(hexStr2ByteArray(strIn),key));
	}

	/**
	 * ��ָ���ַ���������Կ����Կ������ֽ����鳤��Ϊ8λ ����8λʱ���油0������8λֻȡǰ8λ
	 *
	 * @param arrBTmp
	 *            ���ɸ��ַ������ֽ�����
	 * @return ���ɵ���Կ
	 * @throws java.lang.Exception
	 */
	private static Key getKey(byte[] arrBTmp) throws Exception {
		byte[] arrB = new byte[8];// ����һ���յ�8λ�ֽ����飨Ĭ��ֵΪ0��
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {// ��ԭʼ�ֽ�����ת��Ϊ8λ
			arrB[i] = arrBTmp[i];
		}
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");// ������Կ
		return key;
	}
	
	public static void usage(){
		System.out.println("���ܽ��ܹ���");
		System.out.println("java -jar wiscess-util.jar -des [OPTION] <source1> <source2>...");
		System.out.println("");
		System.out.println("ѡ�");
		System.out.println("");
		System.out.println("  /d          ��������ַ������н���");
		System.out.println("  /e          ��������ַ������м���");
		System.out.println("  /k:<key>    ָ��key");
		System.out.println("  <source1>   ������ַ���1");
		System.out.println("  <source2>   ������ַ���2");
	}
	/**
	 * main����
	 */
	public static void main(String[] args) {
//		args=new String[]{"/e","wiscess"};
//		args=new String[]{"/d","ce9e1c1ab7d9f5c2"};
		if(args.length<=1){
			usage();
			return;
		}
		//�Ӳ����б��ж�ȡ��������
		Method method = null;
		String key=strDefaultKey;
		List<String> sources=new ArrayList<String>();
		for(String arg:args){
			if(arg.startsWith("/k:")){
				key=arg.substring(3);
				if(key.equals("")){
					key=strDefaultKey;
				}
			}	
			else if(arg.equalsIgnoreCase("/d")){
				method=Method.decrypt;
			}
			else if(arg.equalsIgnoreCase("/e")){
				method=Method.encrypt;
			}else{
				//�ַ���
				sources.add(arg);
			}
		}
		switch (method) {
		case decrypt:
			//����
			for(String content:sources){
				System.out.println("����ǰ���ַ���:"+content);
				System.out.println("���ܺ���ַ���:"+DesUtils.decrypt(content,key));
			}
			break;
		case encrypt:
			//����
			for(String content:sources){
				System.out.println("����ǰ���ַ���:"+content);
				System.out.println("���ܺ���ַ���:"+DesUtils.encrypt(content,key));
			}
			break;
		default:
			break;
		}
	}
}
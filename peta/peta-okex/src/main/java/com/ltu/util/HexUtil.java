package com.ltu.util;

import com.ltu.model.response.base.MintItemDto;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class HexUtil {
	/*
	 * 字节数组转16进制字符串
	 */
    public static String bytesToHexString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(bArr.length);
        String sTmp;

        for (int i = 0; i < bArr.length; i++) {
            sTmp = Integer.toHexString(0xFF & bArr[i]);
            if (sTmp.length() < 2)
                sb.append(0);
            sb.append(sTmp);
        }

        return sb.toString();
    }

    /**
     * @param n
     * @Title: intTohex
     * @Description: int型转换成16进制
     * @return: String
     */
    public static String intTohex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            s = s.append(b[n % 16]);
            n = n / 16;
        }
        a = s.reverse().toString();
        if ("".equals(a)) {
            a = "00";
        }
        if (a.length() == 1) {
            a = "0" + a;
        }
        return a;
    }
	/**
	 * 异或校验，返回一个字节
	 * @param bytes 待计算校验的字节数组
	 * @return 校验码
	 */
	public static byte orVerification(byte[] bytes){
			int nAll = 0;
			for (int i = 0; i < bytes.length; i++) {
					int nTemp = bytes[i];
					nAll = nAll ^ nTemp;
			}
			return (byte) nAll;
	}

    /**
     * 字符串转16进制字符串
     *
     * @param strPart
     * @return
     */
    public static String string2HexString(String strPart) {
    	
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
        
    }

    /**
     * 十六进制转字节数组
     *
     * @param src
     * @return
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }
    /**
     * 十六进制转字节数组
     *
     * @param src
     * @return
     */
    public static byte[] hexString2Arr(String src) {
    	int l = src.length() / 2;
    	byte[] ret = new byte[l];
    	for (int i = 0; i < l; i++) {
    		ret[i] = (byte) Integer
    				.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
    	}
    	return ret;
    }


    /**
     * Hex字符串转转10进制的byte 
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }


    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }
    /**
     * 十六进制字符串转二进制字符串
     * @param hexStr 十六进制字符串
     * @return 二进制字符串
     */
    public static String hexToBin(String hexStr) {
        String hexToBinStr = "";
        try {
            hexToBinStr = Long.toBinaryString(Long.valueOf(hexStr, 16));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return hexToBinStr;
    }
    public static String[]   hexStrToStrArr(String hexStr) {
	       int l = hexStr.length() / 2;
	        String[] ret = new String[l];
	        for (int i = 0; i < l; i++) {
	            ret[i] = hexStr.substring(i * 2, i * 2 + 2);
	        }
	        ret= hexStrToStrArr2(ret);
	        return ret;
	}
	public static String[]   hexStrToStrArr2(String[]  data) {
		int len= data.length/2;
	      String[]  outArr=new String[len];
	      int j=0;
	      for(int i=0;i<data.length;i=i+2) {
	    	  outArr[j]=data[i+1].concat(data[i]);
	    	  j++;
	      }
	      return outArr;
	}

}



	


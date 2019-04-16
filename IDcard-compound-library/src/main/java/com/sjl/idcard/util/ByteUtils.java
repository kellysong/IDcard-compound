package com.sjl.idcard.util;

/**
 * 字节操作工具类
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ByteUtils.java
 * @time 2017年10月13日 上午8:12:13
 * @copyright(C) 2017 song
 */
public class ByteUtils {

    /**
     * 16进制字符串转字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByteArr(String hex) {
        int l = hex.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(hex.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteArrToHexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString().toUpperCase();
    }

    /**
     * 字节转16进制字符串
     *
     * @param input
     * @param offset
     * @param size
     * @return
     */
    public static String byteToHex(byte[] input, final int offset, final int size) {
        if (input == null)
            return "null";
        StringBuilder sb = new StringBuilder();
        int i;
        for (int d = offset; d < offset + size; d++) {
            i = input[d];
            if (i < 0)
                i += 256;
            if (i < 16)
                sb.append("0");
            sb.append(Integer.toString(i, 16));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 将字符串转成ASCII
     *
     * @param str
     * @return
     */
    public static String stringToAscii(String str) {
        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb.append((int) chars[i]);
        }
        return sb.toString();
    }

    /**
     * 将ASCII转成字符串
     *
     * @param ascii
     * @return
     */
    public static String asciiToString(String ascii) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = ascii.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) chars[i]);
        }
        return sbu.toString();
    }


    /**
     * 调整字节数组
     *
     * @param response
     * @param length
     */
    public static byte[] adjustByteArray(byte[] response, int length) {
        byte[] actual = new byte[length];
        System.arraycopy(response, 0, actual, 0, length);
        return actual;
    }

    /**
     * 获取高四位
     *
     * @param data
     * @return
     */
    public static int getHeight4(byte data) {
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    /**
     * 取出二进制的各位值、
     * 比如：10011
     * 1	1	0	0	1
     *
     * @param num:要获取二进制值的数
     * @param index:倒数第一位为0，依次类推
     */
    public static int getBitValue(int num, int index) {
        return (num & (0x1 << index)) >> index;
    }

    /**
     * 获取低四位
     *
     * @param data
     * @return
     */
    public static int getLow4(byte data) {
        int low;
        low = (data & 0x0f);
        return low;
    }

    /**
     * 字节传字符串
     *
     * @param inBytArr
     * @param offset
     * @param byteCount
     * @return
     */
    public static String byteToString(byte[] inBytArr, int offset, int byteCount) {
        byte[] str = new byte[byteCount];
        System.arraycopy(inBytArr, offset, str, 0, byteCount);
        String res = new String(str);
        return res;
    }

    /**
     * 字节传字符串
     *
     * @param inBytArr
     * @return
     */
    public static String byteToString(byte[] inBytArr) {
        String res = new String(inBytArr);
        return res;
    }


    /**
     * 使用1字节就可以表示b
     *
     * @param b
     * @return
     */
    public static String numToHex8(int b) {
        return String.format("%02x", b);// 2表示需要两个数表示16进制
    }

    /**
     * 使用2字节就可以表示b
     *
     * @param b
     * @return
     */
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }

    /**
     * 使用4字节就可以表示b
     *
     * @param b
     * @return
     */
    public static String numToHex32(int b) {
        return String.format("%08x", b);
    }
}

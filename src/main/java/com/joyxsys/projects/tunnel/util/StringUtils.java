package com.joyxsys.projects.tunnel.util;

/**
 * @ClassName: StringUtils 
 * @Description: 字符串工具类
 * @author robin
 * @date 2015年7月21日 下午10:50:19 
 */
public class StringUtils {
	/**
	 * 系统默认换行符
	 */
	public static String NEWLINE = System.getProperty("line.separator");

	/**
	 * 将整型值字符串转为整形数字
	 * @param orign
	 * 			原始的整型值字符串
	 * @return
	 */
	public static int toInt(String orign) {
		try {
			return Integer.parseInt(orign);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 将布尔值字符串转为boolean型的值
	 * @param orign
	 * 			原始的布尔值字符串
	 * @return
	 */
	public static boolean toBool(String orign) {
		try {
			return Boolean.parseBoolean(orign);
		} catch (Exception e) {
			return false;
		}
	}
}

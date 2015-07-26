package com.joyxsys.projects.tunnel.var;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ConfigurableContants 
 * @Description: 常量配置类，用于读取配置文件 
 * @author robin
 * @date 2015年7月21日 上午11:44:22 
 */
public class ConfigurableContants {

	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(ConfigurableContants.class);

	private static final Properties p = new Properties();

	/**
	 * 初始化方法，实现对配置文件的读取
	 * @param propertyFileName
	 * 				配置文文件路径，相对路径
	 */
	protected static void init(String propertyFileName) {
		InputStream in = null;
		try {
			in = ConfigurableContants.class.getResourceAsStream(propertyFileName);
			if (in != null) {
				p.load(in);
			}
		} catch (IOException e) {
			logger.error("load " + propertyFileName + " into Contants error");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("ConfigurableContants init error...", e);
				}
			}
		}
	}

	/**
	 * 读取配置信息。若读取不到，则使用默认值
	 * @param key
	 * 			配置主键
	 * @param defaultValue
	 * 			默认值
	 * @return
	 */
	public static String getProperty(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

	/**
	 * 读取配置信息
	 * @param key
	 * 			配置主键
	 * @return
	 */
	public static String getProperty(String key) {
		return p.getProperty(key);
	}

	/**
	 * 设置配置信息
	 * @param key
	 * 			主键
	 * @param value
	 * 			配置值
	 * @return
	 */
	public static Object setProperty(String key, String value) {
		return p.setProperty(key, value);
	}
}

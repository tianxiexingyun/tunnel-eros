package com.joyxsys.projects.tunnel.var;

/**
 * @ClassName: Constants 
 * @Description: 常量统一管理类
 * @author robin
 * @date 2015年7月20日 下午2:07:12 
 */
public class Constants extends ConfigurableContants {
	static {
		init("/sys.properties");
	}
	/**
	 * 日志文件在本地存储位置
	 */
	public static final String LOCAl_FILE = getProperty("local_file", "/home/robin/jdevelop/workspace/tunnel-eros/doc/origncode.log");
	/**
	 * 处理中的orign code输入路径,(包括hdfs文件系统和本地文件系统)
	 */
	public static final String INPUT_FILE_PATH = getProperty("input_path", "hdfs://localhost:9000/user/robin/origncode.log");
	/**
	 * 处理结果输出路径，hdfs文件系统
	 */
	public static final String OUTPUT_FILE_PATH = getProperty("output_path", "hdfs://localhost:9000/user/robin/output");
	/**
	 * 输入路径分隔符
	 */
	public static final String INPUT_PATH_SPLIT = ";";
	/**
	 * 源码字段分隔符
	 */
	public static final String ORIGN_CODE_SPLIT = " ";
}

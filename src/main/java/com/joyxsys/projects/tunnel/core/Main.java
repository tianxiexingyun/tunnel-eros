package com.joyxsys.projects.tunnel.core;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joyxsys.projects.tunnel.util.HdfsUtils;
import com.joyxsys.projects.tunnel.var.Constants;

/**
 * @ClassName: Main 
 * @Description: 源码分析程序启动类 
 * @author robin
 * @date 2015年7月25日 上午6:08:03 
 */
public class Main {

	static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		Configuration conf = new Configuration();

		try {
			//设置HdfsUtil使用的配置信息
			HdfsUtils.init(conf);
			//将文件放入HDFS
			HdfsUtils.copyFromLocal(Constants.LOCAl_FILE, Constants.INPUT_FILE_PATH);
			//启动源码分析程序
			new OrignCodeInspector(conf).start();
		} catch (Exception e) {
			logger.error("start orign code inspector error...", e);
		} finally {
			//清理HdfsUtil使用的系统资源
			HdfsUtils.close();
		}

	}
}

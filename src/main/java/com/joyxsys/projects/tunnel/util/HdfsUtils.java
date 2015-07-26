package com.joyxsys.projects.tunnel.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joyxsys.projects.tunnel.var.Constants;

/**
 * @ClassName: HdfsUtils 
 * @Description: hdfs文件操作工具类
 * @author robin
 * @date 2015年7月21日 下午5:05:10 
 */
public class HdfsUtils {

	private static Logger logger = LoggerFactory.getLogger(HdfsUtils.class);

	/**
	 * hadoop配置对象
	 */
	private static final Configuration conf = new Configuration();
	/**
	 * hdfs文件系统对象
	 */
	private static FileSystem fs;

	static {
		init();
	}

	/**
	 * 初始化
	 */
	protected static void init() {
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			logger.error("HdfsUtils init error...", e);
		}
	}

	/**
	 * 重新设置配置信息
	 * @param conf
	 * 			配置文件对象
	 */
	public static void init(Configuration conf) {
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			logger.error("HdfsUtils set configuration error...", e);
		}
	}

	/**
	 * 
	 */
	public static void close() {
		try {
			if (null != fs) {
				fs.close();
			}
		} catch (IOException e) {
			logger.error("HdfsUtils set configuration error...", e);
		}
	}

	/**
	 * 查看目录下的文件
	 * @param path
	 * 			路径
	 * @param reverse
	 * 			是否递归
	 * @return
	 * @throws IOException
	 */
	public static String ls(String path, boolean recursive) throws IOException {
		StringBuilder builder = new StringBuilder();
		Path p = new Path(path);
		FileStatus[] arr = fs.listStatus(p);
		for (int i = 0; i < arr.length; i++) {
			String tmp = arr[i].getPath().toString();
			builder.append(tmp);
			builder.append(StringUtils.NEWLINE);
			if (recursive && arr[i].isDirectory()) {
				builder.append(ls(tmp, recursive));
			}
		}
		return builder.toString();
	}

	/**
	 * 创建目录
	 * @param path
	 * 			目录路径
	 * @throws IOException
	 */
	public static void mkdir(String path) throws IOException {
		Path p = new Path(path);
		fs.create(p);
	}

	/**
	 * 删除目录
	 * @param dirPath
	 * 			目录路径
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteDir(String dirPath) throws IOException {
		Path p = new Path(dirPath);
		return fs.delete(p, true);
	}

	/**
	 * 删除文件
	 * @param path
	 * 			文件路径。如是目录的话会抛出异常
	 * @return
	 * @throws IOException 
	 */
	public static boolean delete(String path) throws IOException {
		Path p = new Path(path);
		return fs.delete(p, false);
	}

	/**
	 * 创建文件，并写入信息，如果文件已经存在将会被覆盖
	 * @param path
	 * 			文件路径
	 * @param s
	 * 			需要写入的字符串
	 * 			
	 * @throws IOException
	 */
	public static void write(String path, String s) throws IOException {
		Path p = new Path(path);
		FSDataOutputStream out = fs.create(p);
		out.writeChars(s);
	}

	/**
	 * 读取文件，并以字符串形式返回文件内容
	 * @param path
	 * 			文件保存路径
	 * @return
	 * @throws IOException
	 */
	public static String read(String path) throws IOException {
		Path p = new Path(path);
		if (!fs.exists(p)) {
			return null;
		}
		FSDataInputStream in = null;
		try {
			in = fs.open(p);
			FileStatus s = fs.getFileStatus(p);
			byte[] bytes = new byte[(int) s.getLen()];
			in.readFully(bytes);
			return new String(bytes);
		} finally {
			if (null != in) {
				in.close();
			}
		}
	}

	/**
	 * 将本地文件系统中的文件拷贝到hdfs文件系统中
	 * @param src
	 * 			本地文件系统中的文件
	 * @param dst
	 * 			hdfs中的文件
	 * @throws IOException
	 */
	public static void put(String src, String dst) throws IOException {
		Path srcPath = new Path(src);
		Path dstPath = new Path(dst);
		fs.copyFromLocalFile(srcPath, dstPath);
	}

	private HdfsUtils() {
	}

	public static void main(String[] args) throws IOException {
		System.out.println(ls(Constants.OUTPUT_FILE_PATH, true));
	}
}

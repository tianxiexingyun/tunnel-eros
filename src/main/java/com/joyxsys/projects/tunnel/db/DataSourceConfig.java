package com.joyxsys.projects.tunnel.db;

import com.joyxsys.projects.tunnel.util.StringUtils;
import com.joyxsys.projects.tunnel.var.ConfigurableContants;

/**
 * @ClassName: DataSourceConfig 
 * @Description: 数据源配置描述类。这次不使用ORM框架，JDBC足够
 * @author robin
 * @date 2015年7月22日 下午10:31:52 
 */
public class DataSourceConfig extends ConfigurableContants {

	static {
		init("/jdbc.properties");
	}

	/**访问路径*/
	public static String url = getProperty("url");
	/**用户名*/
	public static String username = getProperty("username");
	/**密码*/
	public static String password = getProperty("password");
	/**驱动*/
	public static String driver = getProperty("driver");
	/**连接池初始化大小*/
	public static int initialSize = StringUtils.toInt(getProperty("initialSize"));
	/**最大空闲时间*/
	public static int minIdle = StringUtils.toInt(getProperty("minIdle"));
	/**最大连接数*/
	public static int maxActive = StringUtils.toInt(getProperty("maxActive"));
	/**获取连接等待超时的时间，单位是毫秒*/
	public static int maxWait = StringUtils.toInt(getProperty("maxWait"));
	/**间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒*/
	public static int timeBetweenEvictionRunsMillis = StringUtils.toInt(getProperty("timeBetweenEvictionRunsMillis"));
	/**一个连接在池中最小生存的时间，单位是毫秒*/
	public static int minEvictableIdleTimeMillis = StringUtils.toInt(getProperty("minEvictableIdleTimeMillis"));
	/**是否打开PSCache*/
	public static boolean poolPreparedStatements = StringUtils.toBool(getProperty("poolPreparedStatements"));
	/**每个连接上PSCache的大小*/
	public static int maxPoolPreparedStatementPerConnectionSize = StringUtils.toInt(getProperty("maxPoolPreparedStatementPerConnectionSize"));

	private DataSourceConfig() {
	}

}

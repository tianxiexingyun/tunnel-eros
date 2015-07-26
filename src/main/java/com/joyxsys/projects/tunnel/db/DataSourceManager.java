package com.joyxsys.projects.tunnel.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @ClassName: DataSourceManager 
 * @Description: 数据池连接管理 
 * @author robin
 * @date 2015年7月22日 下午10:34:05 
 */
public class DataSourceManager {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

	private static DataSourceManager dataSourceManager = new DataSourceManager();
	private DruidDataSource dataSource;
	private Connection conn;

	public DataSourceManager() {
		try {
			dataSource = new DruidDataSource();
			//基本属性：路径、用户名、密码、驱动
			dataSource.setUrl(DataSourceConfig.url);
			dataSource.setUsername(DataSourceConfig.username);
			dataSource.setPassword(DataSourceConfig.password);
			dataSource.setDriverClassName(DataSourceConfig.driver);

			//配置连接池初始化大小、最小、最大
			dataSource.setInitialSize(DataSourceConfig.initialSize);
			dataSource.setMaxActive(DataSourceConfig.maxActive);
			dataSource.setMinIdle(DataSourceConfig.minIdle);

		} catch (UnsupportedOperationException e) {
			logger.error("数据库连接异常，请求的操作不被支持", e);
		} catch (IllegalArgumentException e) {
			logger.error("数据库连接异常，参数错误", e);
		} catch (Exception e) {
			logger.error("数据库连接异常", e);
		}
	}

	/**
	 * 获取连接池管理实例
	 * @return
	 */
	public static final DataSourceManager getInstance() {
		return dataSourceManager;
	}

	/**
	 * 获取连接实例
	 * @return
	 */
	public final Connection getConnection() {
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			conn = null;
			logger.error("获取数据库连接失败", e);
		}
		return conn;
	}

	/**
	 * 关闭数据源
	 */
	public final void close() {
		try {
			if (null != dataSource) {
				dataSource.close();
			}
			dataSource = null;
		} catch (Exception e) {
			logger.error("关闭数据源失败", e);
		}

	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
}
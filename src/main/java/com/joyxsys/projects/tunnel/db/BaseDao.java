package com.joyxsys.projects.tunnel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: BaseDao 
 * @Description: 数据库操作基类
 * @author robin
 * @date 2015年7月23日 上午10:18:45 
 */
public abstract class BaseDao {

	protected String table;

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 向数据库中插入字段
	 * @param params
	 * 			参数列表
	 * @return
	 */
	protected int insert(Map<String, String> params) {
		Connection conn = null;
		try {
			conn = DataSourceManager.getInstance().getConnection();
			StringBuilder columns = new StringBuilder();
			List<String> values = new ArrayList<String>();
			StringBuilder qmarks = new StringBuilder();
			for (String key : params.keySet()) {
				columns.append(",").append(key);
				qmarks.append(",").append("?");
				values.add(params.get(key));
			}
			StringBuilder sql = new StringBuilder();
			sql.append("insert into ")//
					.append(this.table)//
					.append(" (")//
					.append(columns.substring(1))//
					.append(") values (")//
					.append(qmarks.substring(1))//
					.append(")");
			logger.debug(sql.toString());
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			for (int i = 1; i <= values.size(); i++) {
				pstmt.setString(i, values.get(i - 1));
			}
			return pstmt.executeUpdate();
		} catch (Exception e) {
			logger.error("insert into " + this.table + " error...", e);
			return -1;
		} finally {
			try {
				if (null != conn) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error("close connection for insert error...", e);
			}
		}
	}

	/**
	 * 获取表名称
	 */
	public String getTable() {
		return this.table;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

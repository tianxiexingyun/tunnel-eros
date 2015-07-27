package com.joyxsys.projects.tunnel.dao;

import java.util.HashMap;
import java.util.Map;

import com.joyxsys.projects.tunnel.db.BaseDao;

/**
 * @ClassName: ReportDao 
 * @Description: 统计报表操作类
 * @author robin
 * @date 2015年7月24日 上午6:33:25 
 */
public class ReportDao extends BaseDao {

	public ReportDao() {
		this.table = "report";
	}

	/**
	 * 向数据库中插入记录
	 * @param occurenceTime
	 * 			指令接收时间
	 * @param code
	 * 			指令源码
	 * @return
	 */
	public int insert(String occurenceTime, String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("occurence_time", occurenceTime);
		params.put("orign_code", code);
		return insert(params);
	}

	public static void main(String[] args) {
		new ReportDao().insert("", "  2222 ");
	}
}

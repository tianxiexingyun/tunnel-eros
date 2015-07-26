package com.joyxsys.projects.tunnel.core;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joyxsys.projects.tunnel.dao.ReportDao;
import com.joyxsys.projects.tunnel.util.HdfsUtils;
import com.joyxsys.projects.tunnel.util.StringUtils;
import com.joyxsys.projects.tunnel.var.Constants;

/**
 * @ClassName: OrignCodeCount 
 * @Description: 对tunnel的orign code进行检查，筛选出已删除的 指令记录
 * @author robin
 * @date 2015年7月12日 下午9:31:23 
 *
 */
public class OrignCodeInspector {

	private static final Logger logger = LoggerFactory.getLogger(OrignCodeInspector.class);

	/**
	 * @ClassName: MyMapper 
	 * @Description: Mapper
	 * @author robin
	 * @date 2015年7月12日 下午9:34:45 
	 */
	static class MyMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

		enum COUNTER {
			/**处理中出现异常的记录数目*/
			SKIPPED,
			/**有已删除标记指令数*/
			DELETED,
		}

		@Override
		public void map(LongWritable key, Text value, Context context) {
			try {
				String line = value.toString();
				if (line.contains("deleted")) {
					//对内容作预处理，减少传送的量。其实没必要，因为减少的内容不多。
					String[] arr = line.split(Constants.ORIGN_CODE_SPLIT);
					StringBuilder builder = new StringBuilder();
					builder.append(arr[0])//
							.append(Constants.ORIGN_CODE_SPLIT)//
							.append(arr[1])//
							.append(Constants.ORIGN_CODE_SPLIT)//
							.append(arr[3]);
					//输出map结果
					context.write(NullWritable.get(), new Text(builder.toString()));
					//记录已删除的指令数目
					context.getCounter(COUNTER.DELETED).increment(1L);
				}
			} catch (Exception e) {
				logger.error("map error...", e);
				context.getCounter(COUNTER.SKIPPED).increment(1L);
			}
		}
	}

	/**
	 * @ClassName: MyReducer 
	 * @Description: Reducer
	 * @author robin
	 * @date 2015年7月12日 下午10:03:16
	 */
	static class MyReducer extends Reducer<NullWritable, Text, NullWritable, Text> {

		@Override
		public void reduce(NullWritable key, Iterable<Text> itr, Context context) throws IOException, InterruptedException {

			ReportDao dao = new ReportDao();

			StringBuilder builder = new StringBuilder();
			for (Text tmp : itr) {
				//读出reduce接收到的map结果，将之存入数据库
				String[] arr = tmp.toString().split(Constants.ORIGN_CODE_SPLIT);
				String occurenceTime = arr[0] + " " + arr[1];
				String orignCode = arr[2];
				dao.insert(occurenceTime, orignCode);
				//拼接reducer收到的map结果。
				builder.append(tmp.toString()).append(StringUtils.NEWLINE);
			}
			//输出reduce结果
			context.write(key, new Text(builder.toString()));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf, "orign code check");//指定任务名，根据配置文件创建任务

		job.setJarByClass(OrignCodeInspector.class);

		//指定本次任务使用的Mapper。
		job.setMapperClass(MyMapper.class);
		//指定在slave端的combiner环节使用的Reducer。本次测试数据量较少，该环节可以省略。
		job.setCombinerClass(MyReducer.class);
		//指定在本次任务使用的Reducer
		job.setReducerClass(MyReducer.class);
		//指定输出key的格式
		job.setOutputKeyClass(NullWritable.class);
		//指定输出value的格式
		job.setOutputValueClass(Text.class);

		//添加输入路径
		String inputs = Constants.INPUT_FILE_PATH;
		String[] arr = inputs.split(Constants.INPUT_PATH_SPLIT);
		for (int i = 0; i < arr.length; i++) {
			FileInputFormat.addInputPath(job, new Path(arr[i]));
		}
		//如输出目录已存在，将之删除，防止程序报错
		HdfsUtils.deleteDir(Constants.OUTPUT_FILE_PATH);
		//设置输出路径
		FileOutputFormat.setOutputPath(job, new Path(Constants.OUTPUT_FILE_PATH));

		if (job.waitForCompletion(true)) {
			showOnStdOut();
		}
		//关闭HdfsUtils使用的系统资源
		HdfsUtils.close();
	}

	/**
	 * 在标准输出上显示运行结果
	 */
	private static void showOnStdOut() {

		System.out.println("----------------------------- the result is ----------------------------- ");

		try {
			String results = HdfsUtils.ls(Constants.OUTPUT_FILE_PATH, false);
			String[] arr = results.split(StringUtils.NEWLINE);
			StringBuilder outs = new StringBuilder();
			for (String tmp : arr) {
				outs.append(HdfsUtils.read(tmp));
				outs.append(StringUtils.NEWLINE);
			}
			System.out.println(outs.toString());
		} catch (Exception e) {
			logger.error("print results on stdout error...", e);
		}
	}

}

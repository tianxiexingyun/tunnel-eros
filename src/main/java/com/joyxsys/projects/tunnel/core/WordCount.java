package com.joyxsys.projects.tunnel.core;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 
 * @ClassName: Analyst
 * @Description: 对日志进行分析
 * @author robin
 * @date 2015年7月9日 下午6:07:58
 * 
 */
public class WordCount {

	public static void main(String... args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		if (otherArgs.length < 2) {
			System.err.println("Usage: wordcount <in> [<in>...] <out>");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "word count");//指定任务名，根据配置文件创建任务

		job.setJarByClass(WordCount.class);

		job.setMapperClass(TokenizerMapper.class);//指定使用的Mapper
		job.setCombinerClass(IntSumReducer.class);//指定使用的Reducer
		job.setReducerClass(IntSumReducer.class);

		job.setOutputKeyClass(Text.class);//指定输出key的格式
		job.setOutputValueClass(IntWritable.class);//指定输出value的格式

		for (int i = 0; i < otherArgs.length - 1; i++) {
			FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
		}
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[(otherArgs.length - 1)]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	/**
	 * 
	 * @ClassName: TokenizerMapper 
	 * @Description: 对字符串进行分词计数
	 * @author robin
	 * @date 2015年7月10日 下午3:37:04 
	 *
	 */
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {//
		private static final IntWritable one = new IntWritable(1);
		private final Text word = new Text();

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			//进行分词
			StringTokenizer itr = new StringTokenizer(value.toString());
			//循环处理分词结果，记录每个单词，并将之提交至下一环节
			while (itr.hasMoreTokens()) {
				this.word.set(itr.nextToken());
				context.write(this.word, one);
			}
		}
	}

	/**
	 * 
	 * @ClassName: IntSumReducer 
	 * @Description: TODO 
	 * @author robin
	 * @date 2015年7月10日 下午4:50:57 
	 *
	 */
	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		private final IntWritable result = new IntWritable();

		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			this.result.set(sum);
			context.write(key, this.result);
		}
	}

}
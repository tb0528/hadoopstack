package com.xxo.hdfs.excel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiaoxiaomo on 2016/5/10.
 */
public class ExcelInputFormat extends FileInputFormat<LongWritable,Text>{


	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		return new ExcelRecordReader();
	}


	public class ExcelRecordReader extends RecordReader<LongWritable, Text> {

		private LongWritable key;
		private Text value;
		private InputStream inputStream;
		private String[] strArrayofLines;

		@Override
		public void initialize(InputSplit genericSplit, TaskAttemptContext context)
				throws IOException, InterruptedException {

			FileSplit split = (FileSplit) genericSplit;
			Configuration job = context.getConfiguration();
			final Path file = split.getPath();

			FileSystem fs = file.getFileSystem(job);
			FSDataInputStream fileIn = fs.open(split.getPath());

            inputStream = fileIn;
			String line = new ExcelParser().parseExcelData(inputStream);
			this.strArrayofLines = line.split("\n");
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {

			if (key == null) {
				key = new LongWritable(0);
				value = new Text(strArrayofLines[0]);
			} else {
				if (key.get() < (this.strArrayofLines.length - 1)) {
					long pos = (int) key.get();
					key.set(pos + 1);
					value.set(this.strArrayofLines[(int) (pos + 1)]);
					pos++;
				} else {
					return false;
				}
			}

			if (key == null || value == null) {
				return false;
			} else {
				return true;
			}

		}

		@Override
		public LongWritable getCurrentKey() throws IOException,
				InterruptedException {
			return key;
		}

		@Override
		public Text getCurrentValue() throws IOException, InterruptedException {
			return value;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			return 0;


		}

		@Override
		public void close() throws IOException {
			if (inputStream != null) {
                inputStream.close();
			}
		}
	}
}

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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by xiaoxiaomo on 2014/5/10.
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
			String line = ExcelParser.parseExcelData(inputStream);
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

    public static class ExcelParser {
        public static String parseExcelData(InputStream input) {
            StringBuilder sb = null;
            try {
                //1. 实例化一个HSSFWorkbook
                HSSFWorkbook workbook = new HSSFWorkbook(input);

                //2. 读取第一个Sheet
                HSSFSheet sheet = workbook.getSheetAt(0);

                //3. 遍历sheet的行
                Iterator<Row> rowIterator = sheet.iterator();
                sb = new StringBuilder();
                Row row = null;
                while (rowIterator.hasNext()) {
                    row = rowIterator.next();

                    //3. 获取每一个Cell
                    Iterator<Cell> cellIterator = row.cellIterator();

                    //4. 遍历每一个Cell
                    Cell cell = null;
                    while (cellIterator.hasNext()) {
                        cell = cellIterator.next();

                        //5. 转化类型
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BOOLEAN:
                                sb.append(cell.getBooleanCellValue() + "\t");
                                break;

                            case Cell.CELL_TYPE_NUMERIC:
                                sb.append(cell.getNumericCellValue() + "\t");
                                break;

                            case Cell.CELL_TYPE_STRING:
                                sb.append(cell.getStringCellValue() + "\t");
                                break;

                        }
                    }
                    sb.append("\n");
                }

            } catch (IOException _ex) {
                System.out.println("IO 异常！ " + _ex);
            } finally {
                try {
                    if (input != null)
                        input.close();
                } catch (IOException _ex) {
                    System.out.println("关闭IO 异常！ " + _ex);
                }
            }
            return sb.toString();

        }
    }
}

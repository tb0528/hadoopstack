package com.xxo.hdfs.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaoxiaomo on 2016/5/10.
 */
public class ExcelParser {

    private static Logger logger = LoggerFactory.getLogger(ExcelParser.class);
    private StringBuilder sb = null;
    private long bytesRead = 0;

    public String parseExcelData(InputStream input) {
        try {
            //1. 实例化一个HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook(input);

            //2. 读取第一个Sheet
            HSSFSheet sheet = workbook.getSheetAt(0);

            //3. 遍历sheet的行
            Iterator<Row> rowIterator = sheet.iterator();
            sb = new StringBuilder();
            Row row = null ;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();

                //3. 获取每一个Cell
                Iterator<Cell> cellIterator = row.cellIterator();

                //4. 遍历每一个Cell
                Cell cell = null ;
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();

                    //5. 转化类型
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            bytesRead++;
                            sb.append(cell.getBooleanCellValue() + "\t");
                            break;

                        case Cell.CELL_TYPE_NUMERIC:
                            bytesRead++;
                            sb.append(cell.getNumericCellValue() + "\t");
                            break;

                        case Cell.CELL_TYPE_STRING:
                            bytesRead++;
                            sb.append(cell.getStringCellValue() + "\t");
                            break;

                    }
                }
                sb.append("\n");
            }

        } catch (IOException _ex ) {
            logger.error("IO 异常！ " + _ex);
        } finally {
            try {
                if( input != null )
                    input.close() ;
            } catch (IOException _ex) {
                logger.error("关闭IO 异常！ " + _ex);
            }
        }
        return sb.toString();

    }

    public long getBytesRead() {
        return bytesRead;
    }

}

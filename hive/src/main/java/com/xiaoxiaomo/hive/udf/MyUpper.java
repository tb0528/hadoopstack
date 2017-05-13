package com.xiaoxiaomo.hive.udf;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 *
 * Created by xiaoxiaomo on 2015/5/6.
 */
@Description(name = "MyUpper",
		value = " _FUNC_(str) - Returns str with all characters changed to uppercase",
		extended = "Example:\n"
			+ " > SELECT _FUNC_(name) FROM src;")
public class MyUpper extends UDF {

	/**
	 * 该方法可以重载
	 * @param text
	 * @return
	 */
	public Text evaluate(Text text) {
		if(text != null) {
			return new Text(text.toString().toUpperCase());
		} else {
			return null;
		}
	}
}
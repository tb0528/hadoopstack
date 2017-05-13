package com.xiaoxiaomo.hbase.pro.utils;

import java.sql.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * c3p0连接池用法
 * Created by xiaoxiaomo on 2015/4/26.
 *
 */
public class C3P0Utils {

	static ComboPooledDataSource cpds = new ComboPooledDataSource();

	public static final String INSERT_LOG = "INSERT INTO spider(goods_id,data_url,pic_url,title,price,param,`current_time`) VALUES(?,?,?,?,?,?,?)";

	// 获取连接
	private static Connection getConnection() throws SQLException {
		return  cpds.getConnection();
	}



	public static void update(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
			conn = getConnection();
            statement = conn.prepareStatement(sql);
            String[] p =  params.toString().split(",");
            for (int i = 1; i < 8; i++) {
                statement.setString(i ,p[i] );
            }
            statement.executeUpdate() ;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

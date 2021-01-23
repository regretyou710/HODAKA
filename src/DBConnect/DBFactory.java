package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DBFactory {
	private static Connection conn = null;
	
	private DBFactory() {

	}

	public static Connection getOracleDBConnection(String host, String DBName, String username, String password) {
		try {
			if (conn == null || conn.isClosed()) {
				final String URL = "jdbc:oracle:thin:@" + host + ":" + DBName;
				conn = DriverManager.getConnection(URL, username, password);
				
				// 預定義的標準格式
				DateTimeFormatter isoLocalDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
				// 格式化:日期->字串
				String dateTime = isoLocalDateTime.format(LocalDateTime.now());
				System.out.println("連線時間:" + dateTime);
			}

		} catch (SQLException e) {
			System.out.println("SQL執行錯誤=>" + e.getMessage());
			System.out.println(e.getStackTrace());
		}
		return conn;
	}
}

package 每日生產報表檢查;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class 每日生產報表檢查Testing {
	public static void main(String[] args) {
		try {
			Properties properties = new Properties();

			// 使用ClassLoader載入properties配置檔案生成對應的輸入流
			InputStream is = 每日生產報表檢查Testing.class.getClassLoader().getResourceAsStream("每日生產報表檢查/SQLSTR.properties");
			// 使用properties物件載入輸入流
			properties.load(is);

			// 獲取key對應的value值
			for (Object k : properties.keySet()) {
				String key = (String) k;
				System.out.println(k + "筆數:" + ReportCheck.reportCheckMethod(key, 1, 1));
			}
		} catch (IOException e) {
			System.out.println("IO執行錯誤=>" + e.getMessage());
			System.out.println(e.getStackTrace());
		} finally {
			try {
				Connection conn = ReportCheck.getConn();
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("關閉連線錯誤=>" + e.getMessage());
				System.out.println(e.getStackTrace());
			}
		}
	}
}

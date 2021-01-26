package ReportCheck;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import DBConnect.DBFactory;
import ReportCheck.tw.com.bean.Message;

public class ReportCheckMethod {
	private static Connection conn = null;
	private static Logger logger = null;

	static {
		logger = LogManager.getLogger("RollingRandomAccessFileLogger");
		try {
			Properties properties = new Properties();

			// 使用ClassLoader載入properties配置檔案生成對應的輸入流
			InputStream is = ReportCheckMethod.class.getClassLoader().getResourceAsStream("connectConfig.properties");

			// 使用properties物件載入輸入流
			properties.load(is);

			// 獲取key對應的value值
			String host = properties.getProperty("host_1");
			String DBName = properties.getProperty("DBName_1");
			String username = properties.getProperty("username_1");
			String password = properties.getProperty("password_1");
			logger.debug("host:" + host);
			logger.debug("DBName:" + DBName);
			logger.debug("username:" + username);
			logger.debug("password:" + password);
			conn = DBFactory.getOracleDBConnection(host, DBName, username, password);
		} catch (IOException e) {
			logger.error("IO執行錯誤=>" + e.getMessage());
			logger.error(e.getStackTrace());
		}
	}

	private ReportCheckMethod() {

	}

	public static Connection getConn() {
		return conn;
	}

	public static List<Message> reportCheckMethod(String sqlStr, int endDay) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int startDay = dayChoose();
		ArrayList<Message> msgList = null;
		try {
			pstmt = conn.prepareStatement(sqlStr);

			// 指派參數
			pstmt.setInt(1, startDay);
			pstmt.setInt(2, endDay);
			rs = pstmt.executeQuery();

			msgList = new ArrayList<Message>();
			while (rs.next()) {
				Message message = new Message();
				message.setTxndate(rs.getString(1));
				message.setCount(rs.getInt(2));
				msgList.add(message);

				logger.debug(rs.getString(1));
				logger.debug(rs.getInt(2));
			}
		} catch (SQLException e) {
			logger.error("SQL執行錯誤=>" + e.getMessage());
			logger.error(e.getStackTrace());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				logger.error("關閉連線錯誤=>" + e.getMessage());
				logger.error(e.getStackTrace());
			}

			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				logger.error("關閉連線錯誤=>" + e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
		return msgList;
	}

	private static int dayChoose() {
		LocalDateTime localDateTime = LocalDateTime.now();
		int dayOfWeekVal = localDateTime.getDayOfWeek().getValue();
		int dayOfWeek;
		switch (dayOfWeekVal) {
		case 1:
			dayOfWeek = 3;
			break;

		default:
			dayOfWeek = 1;
			break;
		}
		return dayOfWeek;
	}
}

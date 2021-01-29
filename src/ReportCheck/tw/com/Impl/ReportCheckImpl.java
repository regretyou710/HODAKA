package ReportCheck.tw.com.Impl;

import java.io.IOException;
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
import ReportCheck.tw.com.dao.IReportCheck;
import ReportCheck.tw.com.domain.ReportCheck;
import ResourceFileLoader.ResourceFileLoaderUtilImpl;

public class ReportCheckImpl implements IReportCheck {
	private Connection conn = null;
	private Logger logger = null;
	private Properties properties = null;
	private ResourceFileLoaderUtilImpl rfl = null;
	private Version version = null;
	private String v1 = "2.2";
	private String v2 = null;

	public ReportCheckImpl() {
		logger = LogManager.getLogger("RollingRandomAccessFileLogger");
		try {
			// 使用ClassLoader載入properties配置檔案生成對應的輸入流
			// 使用properties物件載入輸入流
			rfl = new ResourceFileLoaderUtilImpl();
			properties = rfl.getResourceFileAtSoruce("connectConfig.properties");

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
			logger.debug(conn.getSchema());

			version = new Version();
			String sql = "SELECT * FROM XX_REPORTCHECK_VER "
					+ "OFFSET (SELECT COUNT(*) from XX_REPORTCHECK_VER)-1 ROWS FETCH NEXT 1 ROWS ONLY";

			v2 = version.getVerNumQuery(version.getConn(), sql);
		} catch (IOException e) {
			logger.error("IO執行錯誤=>" + e.getMessage());
			logger.error(e.getStackTrace());
		} catch (SQLException e) {
			logger.error("SQL執行錯誤=>" + e.getMessage());
			logger.error(e.getStackTrace());
		}
	}

	public Connection getConn() {
		return conn;
	}

	public Version getVersion() {
		return version;
	}

	public String getV1() {
		return v1;
	}

	public String getV2() {
		return v2;
	}

	@Override
	public List<ReportCheck> getReportQuery(String sqlStr, int endDay) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int startDay = dayChoose();
		ArrayList<ReportCheck> msgList = null;
		try {
			pstmt = conn.prepareStatement(sqlStr);

			// 指派參數
			pstmt.setInt(1, startDay);
			pstmt.setInt(2, endDay);
			rs = pstmt.executeQuery();

			msgList = new ArrayList<ReportCheck>();
			while (rs.next()) {
				ReportCheck reportCheck = new ReportCheck();
				reportCheck.setTxndate(rs.getString(1));
				reportCheck.setCount(rs.getInt(2));
				msgList.add(reportCheck);

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

	@Override
	public int dayChoose() {
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

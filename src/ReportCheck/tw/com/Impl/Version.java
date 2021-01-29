package ReportCheck.tw.com.Impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import DBConnect.DBFactory;
import ResourceFileLoader.ResourceFileLoaderUtilImpl;
import VersionCompare.VersionCompareUtilImpl;

public class Version extends VersionCompareUtilImpl {
	private Connection conn = null;
	private Logger logger = null;
	private Properties properties = null;
	private ResourceFileLoaderUtilImpl rfl = null;

	public Version() {
		logger = LogManager.getLogger("RollingRandomAccessFileLogger");
		try {
			// 使用ClassLoader載入properties配置檔案生成對應的輸入流
			// 使用properties物件載入輸入流
			rfl = new ResourceFileLoaderUtilImpl();
			properties = rfl.getResourceFileAtSoruce("connectConfig.properties");

			// 獲取key對應的value值
			String host = properties.getProperty("host_2");
			String DBName = properties.getProperty("DBName_2");
			String username = properties.getProperty("username_2");
			String password = properties.getProperty("password_2");
			logger.debug("host:" + host);
			logger.debug("DBName:" + DBName);
			logger.debug("username:" + username);
			logger.debug("password:" + password);
			conn = DBFactory.getOracleDBConnection(host, DBName, username, password);
			logger.debug(conn.getSchema());
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

	@Override
	public String getVerNumQuery(Connection conn, String sqlStr) {		
		String verNum = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlStr);
			if (rs.next()) {
				verNum = rs.getString(2);
				logger.debug(rs.getString(2));
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
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				logger.error("關閉連線錯誤=>" + e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
		return verNum;
	}

	@Override
	public void addVerNum(Connection conn, String sqlStr, String verNumber) {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sqlStr);
			
			String uuid = "";
			synchronized (uuid) {
				uuid = UUID.randomUUID().toString().replace("-", "");
			}
			
			// 自定義的格式
			DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			String dateTime = ofPattern.format(LocalDateTime.now());
			
			pstmt.setString(1, uuid);
			pstmt.setString(2, verNumber);
			pstmt.setString(3, dateTime);

			int row = pstmt.executeUpdate();
			if (row > 0) {
				logger.debug("版次新增:" + row + "筆");
				System.out.println("新增成功!");
			}
		} catch (SQLException e) {
			logger.error("SQL執行錯誤=>" + e.getMessage());
			logger.error(e.getStackTrace());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				logger.error("關閉連線錯誤=>" + e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}

}

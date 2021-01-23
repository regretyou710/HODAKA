package 每日生產報表檢查;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBConnect.DBFactory;

public class ReportCheck {
	private static Connection conn = DBFactory.getOracleDBConnection("192.168.200.246:1521", "wrp", "wrp",
			"wrp5050560");

	private ReportCheck() {

	}

	public static Connection getConn() {
		return conn;
	}

	public static int reportCheckMethod(String sqlStr, int startDay, int endDay) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sqlStr);

			// 指派參數
			pstmt.setInt(1, startDay);
			pstmt.setInt(2, endDay);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			System.out.println("SQL執行錯誤=>" + e.getMessage());
			System.out.println(e.getStackTrace());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("關閉連線錯誤=>" + e.getMessage());
				System.out.println(e.getStackTrace());
			}
		}
		return count;
	}

}

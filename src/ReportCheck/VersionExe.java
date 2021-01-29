package ReportCheck;

import java.sql.Connection;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ReportCheck.tw.com.Impl.ReportCheckImpl;
import ReportCheck.tw.com.Impl.Version;

public class VersionExe {

	public static void main(String[] args) {
		Logger logger = LogManager.getLogger("RollingRandomAccessFileLogger");
		boolean flag = true;
		do {
			Scanner sc = new Scanner(System.in);
			System.out.println("新增版本序號:");
			String vernum = sc.nextLine().trim();

			// 執行版本序號新增
			Version version = new Version();
			ReportCheckImpl reportCheckImpl = new ReportCheckImpl();
			Connection conn = reportCheckImpl.getVersion().getConn();
			String sql = "INSERT INTO XX_REPORTCHECK_VER(UUID,VERNUM,UPDATETIME) VALUES (?,?,?)";

			/*
			 * 整數位:[1-9]開頭後，\d*任意數字出現零至多次
			 * 第一個小數點:\.{1}小數點至少出現一次後，\d+任意數出現1至多次
			 * 第二的小數點:\.{1}小數點出現零到一次後，\d+任意數出現1至多次，整個括弧起來出現零至1次
			 */
			if (vernum.matches("[1-9]\\d*\\.{1}\\d+(\\.{1}\\d+){0,1}")) {
				if (version.compareVersion(reportCheckImpl.getV2(), vernum)==1) {
					logger.debug("新增失敗!輸入版本序號較舊，請重新輸入...");
					System.out.println("新增失敗!輸入版本序號較舊，請重新輸入...");					
				}else {
					version.addVerNum(conn, sql, vernum);
					flag = false;					
				}				
			} else {
				logger.debug("新增失敗!請輸入版本正確格式...");
				System.out.println("新增失敗!請輸入版本正確格式...");
			}
		} while (flag);
	}

}

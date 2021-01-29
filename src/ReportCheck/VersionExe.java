package ReportCheck;

import java.sql.Connection;
import java.util.Scanner;

import ReportCheck.tw.com.Impl.ReportCheckImpl;
import ReportCheck.tw.com.Impl.Version;

public class VersionExe {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("新增版本序號:");
		String vernum = sc.nextLine().trim();

		// 執行版本序號新增
		Version version = new Version();
		ReportCheckImpl reportCheckImpl = new ReportCheckImpl();
		Connection conn = reportCheckImpl.getVersion().getConn();
		String sql = "INSERT INTO XX_REPORTCHECK_VER(UUID,VERNUM,UPDATETIME) VALUES (?,?,?)";
		version.addVerNum(conn, sql, vernum);
	}

}

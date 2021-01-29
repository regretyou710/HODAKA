package ReportCheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ReportCheck.tw.com.Impl.ReportCheckImpl;
import ReportCheck.tw.com.bean.SQLString;
import ReportCheck.tw.com.domain.ReportCheck;
import VersionCompare.VersionCompareUtilImpl;

public class ReportCheckExe {
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger("RollingRandomAccessFileLogger");
		ReportCheckImpl reportCheckImpl = new ReportCheckImpl();
		InputStreamReader isr = null;
		BufferedReader br = null;

		String v1 = reportCheckImpl.getV1();
		String v2 = reportCheckImpl.getV2();
		VersionCompareUtilImpl verCompare = new VersionCompareUtilImpl();
		logger.debug("目前版本:" + v1);
		logger.debug("最新版本:" + v2);
		logger.info("目前版本:v" + v1);
		if (verCompare.compareVersion(v1, v2) == 1) {
			logger.debug("目前是最新版本");
		} else if (verCompare.compareVersion(v1, v2) == 0) {
			logger.debug("版本相同");
		} else if (verCompare.compareVersion(v1, v2) == -1) {
			logger.info("有最新版本，建議使用新版本");
		}

		try {
			String path = "ReportCheck/SQLSTR.json";

			InputStream is = ReportCheckExe.class.getClassLoader().getResourceAsStream(path);
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);

			// 透過gson對json文件讀取
			Gson gson = new Gson();
			List<SQLString> SQLObjs = gson.fromJson(br, new TypeToken<List<SQLString>>() {
			}.getType());

			List<ReportCheck> msgList = null;
			for (SQLString obj : SQLObjs) {
				logger.info("------------------------");
				logger.info(obj.getReportName());
				msgList = reportCheckImpl.getReportQuery(obj.getSql(), 1);
				int sum = 0;
				for (int i = 0; i < msgList.size(); i++) {
					logger.info("日期:" + msgList.get(i).getTxndate() + " 筆數:" + msgList.get(i).getCount());
					sum += msgList.get(i).getCount();
				}

				if (msgList.size() > 1) {
					logger.info("總筆數:" + sum);
				} else if (msgList.size() == 0) {
					logger.info("無資料");
				}

				logger.debug(obj.getReportName());
				logger.debug(obj.getSql());
			}
		} catch (IOException e) {
			logger.error("IO執行錯誤=>" + e.getMessage());
			logger.error(e.getStackTrace());
		} finally {
			try {
				logger.debug(reportCheckImpl.getConn().isClosed());
				logger.debug(reportCheckImpl.getConn());
				if (reportCheckImpl.getConn() != null && !reportCheckImpl.getConn().isClosed()) {
					reportCheckImpl.getConn().close();
				}
				logger.debug(reportCheckImpl.getConn().isClosed());
				logger.debug(reportCheckImpl.getConn());
			} catch (SQLException e) {
				logger.error("關閉連線錯誤=>" + e.getMessage());
				logger.error(e.getStackTrace());
			}

			try {
				logger.debug(reportCheckImpl.getVersion().getConn().isClosed());
				logger.debug(reportCheckImpl.getVersion().getConn());
				if (reportCheckImpl.getVersion().getConn() != null
						&& !reportCheckImpl.getVersion().getConn().isClosed()) {
					reportCheckImpl.getVersion().getConn().close();
				}
				logger.debug(reportCheckImpl.getVersion().getConn().isClosed());
				logger.debug(reportCheckImpl.getVersion().getConn());
			} catch (SQLException e) {
				logger.error("關閉連線錯誤=>" + e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}
}

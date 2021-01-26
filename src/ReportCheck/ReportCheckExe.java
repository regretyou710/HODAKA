package ReportCheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ReportCheck.tw.com.bean.Message;
import ReportCheck.tw.com.bean.SQLString;

public class ReportCheckExe {
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger("RollingRandomAccessFileLogger");
		InputStreamReader isr =null;
		BufferedReader br = null;
		try {
			/*
			Properties properties = new Properties();

			// 使用ClassLoader載入properties配置檔案生成對應的輸入流
			InputStream is = 每日生產報表檢查Testing.class.getClassLoader().getResourceAsStream("每日生產報表檢查/SQLSTR.properties");
			// 使用properties物件載入輸入流
			properties.load(is);

			// 獲取key對應的value值
			List<Message> msgList = null;
			SortedMap sortedMap = new TreeMap(properties);
			Set<Object> sqlStr = sortedMap.keySet();
			for (Object k : sqlStr) {
				String key = (String) k;
				logger.info("------------------------");
				logger.info(key);
				msgList = ReportCheck.reportCheckMethod(properties.getProperty(key), 1);

				int sum = 0;
				for (int i = 0; i < msgList.size(); i++) {
					logger.info("日期:" + msgList.get(i).getTxndate() + " 筆數:" + msgList.get(i).getCount());
					sum += msgList.get(i).getCount();
				}
				logger.info("總筆數:" + sum);
			}
			*/

			// ******************************************************

			String path = "ReportCheck/SQLSTR.json";
			
			InputStream is = ReportCheckExe.class.getClassLoader().getResourceAsStream(path);
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			
			//透過gson對json文件讀取
			Gson gson = new Gson();
			List<SQLString> SQLObjs = gson.fromJson(br, new TypeToken<List<SQLString>>() {
			}.getType());
			
			List<Message> msgList = null;
			for (SQLString obj : SQLObjs) {				
				logger.info("------------------------");
				logger.info(obj.getReportName());
				msgList = ReportCheckMethod.reportCheckMethod(obj.getSql(), 1);
				int sum = 0;
				for (int i = 0; i < msgList.size(); i++) {
					logger.info("日期:" + msgList.get(i).getTxndate() + " 筆數:" + msgList.get(i).getCount());
					sum += msgList.get(i).getCount();
				}
				
				logger.info("總筆數:" + sum);
				logger.debug(obj.getReportName());
				logger.debug(obj.getSql());
			}
		} catch (IOException e) {
			logger.error("IO執行錯誤=>" + e.getMessage());
			logger.error(e.getStackTrace());
		} finally {
			
			try {
				Connection conn = ReportCheckMethod.getConn();
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("關閉連線錯誤=>" + e.getMessage());
				logger.error(e.getStackTrace());
			}
		}
	}
}

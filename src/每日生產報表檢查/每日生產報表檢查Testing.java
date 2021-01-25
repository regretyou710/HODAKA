package 每日生產報表檢查;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import 每日生產報表檢查.tw.com.bean.Message;
import 每日生產報表檢查.tw.com.bean.SQLString;

public class 每日生產報表檢查Testing {
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger("RollingRandomAccessFileLogger");

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

			String path = "src/每日生產報表檢查/SQLSTR.json";
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(new File(path)));
			
			Gson gson = new Gson();
			List<SQLString> datas = gson.fromJson(br, new TypeToken<List<SQLString>>() {
			}.getType());
			
			List<Message> msgList = null;
			for (SQLString obj : datas) {				
				logger.info("------------------------");
				logger.info(obj.getReportName());
				msgList = ReportCheck.reportCheckMethod(obj.getSql(), 1);
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
				Connection conn = ReportCheck.getConn();
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

package ReportCheck.tw.com.dao;

import java.util.List;

import ReportCheck.tw.com.domain.ReportCheck;


public interface IReportCheck {
	// 查詢報表紀錄
	public List<ReportCheck> getReportQuery(String sqlStr, int endDay);
	
	//日期對應星期
	public int dayChoose();
}

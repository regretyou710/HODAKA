package ReportCheck.tw.com.bean;

import java.io.Serializable;

public class SQLString implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 58152329540640816L;
	private String reportName;
	private String sql;

	public SQLString() {
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String toString() {
		return "SQLString [reportName=" + reportName + ", sql=" + sql + "]";
	}

}

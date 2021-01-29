package ReportCheck.tw.com.domain;

import java.io.Serializable;

public class ReportCheck implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3122598896483926508L;
	private String txndate;
	private int count;

	public ReportCheck() {

	}

	public String getTxndate() {
		return txndate;
	}

	public void setTxndate(String txndate) {
		this.txndate = txndate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Message [txndate=" + txndate + ", count=" + count + "]";
	}
	
}

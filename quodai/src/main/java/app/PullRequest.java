package app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PullRequest {
	private Date startTime;
	private Date endTime;
	private long id;

	public PullRequest(long id, Date startDate) {
		this.setId(id);
		this.startTime = startDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PullRequest(long id, String startDate) {
		this.setId(id);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ssZ");
		try {
			this.startTime = formatter.parse(startDate.replaceAll("Z$", "+0000"));
		} catch (ParseException e) {
			System.out.println("Parse Date error");
		}
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setStartTime(String startTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ssZ");
		try {
			this.startTime = formatter.parse(startTime.replaceAll("Z$", "+0000"));
		} catch (ParseException e) {
			System.out.println("Parse Date error");
		}
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setEndTime(String endTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ssZ");
		try {
			this.endTime = formatter.parse(endTime.replaceAll("Z$", "+0000"));
		} catch (ParseException e) {
			System.out.println("Parse Date error");
		}
	}

}
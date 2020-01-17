package app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commits {
	private Date date;
	private int numCommits = 0;

	public Commits(Date date, int commit) {
		this.date = date;
		this.numCommits += commit;
	}

	// Contructor Commits
	// Parameters: date: string - format YYYY-mm-ddThh:mm:ssZ
	// commit: int number commit
	public Commits(String date, int commit) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		try {
			this.date = formatter.parse(date);
		} catch (ParseException e) {
			System.out.println("Parse Date error");
			// e.printStackTrace();
		}

		this.numCommits += commit;
	}

	public int getNumCommits() {
		return numCommits;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setNumCommitsperDay(int numCommits) {
		this.numCommits = numCommits;
	}

	public void add(int num) {
		this.numCommits += num;
	}
}
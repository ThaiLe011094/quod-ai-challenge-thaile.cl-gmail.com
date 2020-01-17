package app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RunApp {

	public static final int MAX_CONTRIBUTOR = 1339;
	public static final int MAX_COMMIT = 724299; // Update 2017
	public static final int MAX_COMMIT_PER_DAY = 6000;
	private DataPrepair pre;
	public Owner owner;

	public RunApp() {
		pre = new DataPrepair();
		owner = new Owner();
	}

	public DataPrepair argFunc(String[] args) {
		DataPrepair _data = new DataPrepair();
		if (args.length < 2)
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
		try {
			_data.startDate = formatter.parse(args[0].replaceAll("Z$", "+0000"));
			_data.endDate = formatter.parse(args[1].replaceAll("Z$", "+0000"));
			return _data;
		} catch (ParseException e) {
			System.out.println("Wrong argument");
			return null;
		}
	}

	public boolean download(Date startDate, Date endDate) throws IOException {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		end.add(Calendar.HOUR, 1);

		for (Date date = start.getTime(); start.before(end); start.add(Calendar.HOUR, 1), date = start.getTime()) {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			cal.setTime(date);
			int _hour = cal.get(Calendar.HOUR);
			int _day = cal.get(Calendar.DAY_OF_MONTH);
			int _month = cal.get(Calendar.MONTH) + 1;
			int _year = cal.get(Calendar.YEAR);
			String fileDownload = String.format("%d-%02d-%02d-%d", _year, _month, _day, _hour);
			System.out.println("Downloading " + fileDownload + ".json.gz ....");

			if (new File("./data/").exists()) {
				continue;
			} else {
				try {
					File file = new File("./data/");
					file.mkdir();
				} catch (Exception e) {
					System.out.println("Could not create data folder");
				}
			}

			if (new File("./data/" + fileDownload + ".json.gz").exists()) {
				continue;
			}
			BufferedInputStream inputStream = new BufferedInputStream(
					new URL("https://data.gharchive.org/" + fileDownload + ".json.gz").openStream());
			FileOutputStream fileOS = new FileOutputStream("./data/" + fileDownload + ".json.gz");
			byte data[] = new byte[1024];
			int byteContent;
			while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
				fileOS.write(data, 0, byteContent);
			}
		}

		System.out.println("Download Finish!!");
		return true;
	}

	public void readData2CSV(String file) throws IOException, org.json.simple.parser.ParseException {
		System.out.println("Reading and processing file " + file + " ....");
		InputStream fileStream = new FileInputStream(file);
		InputStream gzipStream = new GZIPInputStream(fileStream);

		Reader decoder = new InputStreamReader(gzipStream);
		BufferedReader buffered = new BufferedReader(decoder);
		String text;
		while ((text = buffered.readLine()) != null) {
			JSONParser pa = new JSONParser();
			JSONObject jo = (JSONObject) pa.parse(text);
			process(jo);
		}
		;
		gzipStream.close();
		fileStream.close();
	}

	private void process(JSONObject objects) {
		String id = (String) objects.get("id");
		JSONObject actor = (JSONObject) objects.get("actor");
		long idActor = (long) actor.get("id");
		String type = (String) objects.get("type");
		JSONObject repo = (JSONObject) objects.get("repo");
		JSONObject payload = (JSONObject) objects.get("payload");
		long idRepo = (long) repo.get("id");
		String nameRepo = (String) repo.get("name");
		String createdAt = (String) objects.get("created_at");
		owner.addRepo(idRepo, nameRepo);
		owner.addContributor(idRepo, idActor);
		switch (type) {
		case "PushEvent":
			JSONArray commits = (JSONArray) payload.get("commits");
			owner.updateRepo(idRepo, nameRepo, createdAt, commits.size());
			break;
		case "PullRequestEvent":
			String actionRQ = (String) payload.get("action");
			JSONObject pullRequest = (JSONObject) payload.get("pull_request");
			long idRQ = (long) pullRequest.get("id");
			boolean merge = (boolean) pullRequest.get("merged");
			if (actionRQ.compareTo("opened") == 0) {
				owner.updatePullRequest(idRepo, idRQ, createdAt, null);
			} else if ((actionRQ.compareTo("closed") == 0) & (merge)) {
				owner.updatePullRequest(idRepo, idRQ, null, createdAt);
			}
			break;
		case "IssuesEvent":
			String action = (String) payload.get("action");
			JSONObject issue = (JSONObject) payload.get("issue");
			long idIssue = (long) issue.get("id");
			if (action.compareTo("closed") == 0) {
				owner.updateIssue(idRepo, idIssue, null, createdAt);
			} else if (action.compareTo("opened") == 0) {
				owner.updateIssue(idRepo, idIssue, createdAt, null);
			}
			break;
		}

	}

	public synchronized void update(Owner own) {
		System.out.println("Update owner");
		owner.updateOwner(own);
		System.out.println("Update Success");
	}

}
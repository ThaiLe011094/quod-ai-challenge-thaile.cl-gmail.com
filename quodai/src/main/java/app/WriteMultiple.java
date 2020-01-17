package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.Semaphore;

public class WriteMultiple extends Thread {
	static Semaphore semaphore = new Semaphore(10); // Max semaphore :10
	private Repo[] repo;
	private String file;

	public WriteMultiple(String file, Repo[] _repo) {
		this.file = file;
		this.repo = _repo;
	}

	@Override
	public void run() {
		try {
//			System.out.println("writing CSV ....");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(
					"org,repo,health_score,num_commit,num_contributor,num_commit_per_day,time_issue,time_pull_to_merge,commit_per_developer\n");
			for (int i = 0; i < repo.length; i++) {
				long totalCommit = repo[i].getTotalCommit();
				int numContributor = repo[i].getNumContributors();
				long numCommitPerDay = repo[i].getNumCommitsperDay();
				float healthScore = totalCommit / RunApp.MAX_COMMIT + numCommitPerDay / RunApp.MAX_COMMIT_PER_DAY
						+ numContributor / RunApp.MAX_CONTRIBUTOR;
				repo[i].setHealthScore(healthScore);
				String format = String.format("%s,%.2f,%d,%d,%d,%d,%d,%f\n", repo[i].getName().replace("/", ","),
						healthScore, totalCommit, numContributor, numCommitPerDay, repo[i].averageTimeIssue(),
						repo[i].averageTimeRQ2Merge(), repo[i].commitPerDev());
				writer.append(format);
			}
//			System.out.println("Wrote at " + file);
			writer.close();
		} catch (Exception e) {
			// Throwing an exception
			System.out.println("Exception is caught in Write");
		}
	}
}
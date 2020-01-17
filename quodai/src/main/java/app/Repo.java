package app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.IntStream;

public class Repo {

	private long id;
	private String name;
	private Commits[] numCommits;
	private Issue[] issue;
	private PullRequest[] pullRequest;
	private int ratioCommitsperMenmBer;
	private long totalCommit;
	private long[] memberId;
	private float healthScore;

	public Repo() {
	}

	public float getHealthScore() {
		return healthScore;
	}

	public void setHealthScore(float healthScore) {
		this.healthScore = healthScore;
	}

	public long getTotalCommit() {
		totalCommit();
		return totalCommit;
	}

	public void setTotalCommit(long totalCommit) {
		this.totalCommit = totalCommit;
	}

	public Repo(long id, String name) {
		this.setId(id);
		this.setName(name);
		// this.memberId = new long[1];
	}

	public void updateParams(Repo repo) {
		if (repo.numCommits != null) {
			addMembers(repo.memberId);
		}
		if (repo.numCommits != null) {
			for (Commits com : repo.numCommits) {
				updateCommits(com);
			}
		}
		if (repo.issue != null) {
			for (Issue _issue : repo.issue) {
				updateIssue(_issue);
			}
		}
		if (repo.pullRequest != null) {
			for (PullRequest _pull : repo.pullRequest) {
				updatePullRequest(_pull);
			}
		}
	}

	public void addCommits(Commits commits) {
		Commits[] _commit = new Commits[1];
		Commits[] _tmp = this.numCommits;
		_commit[0] = commits;
		this.numCommits = new Commits[_tmp.length + _commit.length];
		System.arraycopy(_tmp, 0, numCommits, 0, _tmp.length);
		System.arraycopy(_commit, 0, numCommits, _tmp.length, 1);
	}

	public void updateCommits(Commits commits) {
		int i;
		Commits[] _commit = new Commits[1];
		_commit[0] = commits;
		if ((i = index(commits.getDate())) >= 0) {
			numCommits[i].add(commits.getNumCommits());
		} else {
			if (numCommits == null) {
				numCommits = new Commits[1];
				System.arraycopy(_commit, 0, numCommits, 0, 1);
				return;
			}
			this.addCommits(commits);
		}
	}

	public void updateCommits(String date, int num) {
		int i;
		if ((i = index(date)) >= 0) {
			numCommits[i].add(num);
		} else {
			Commits[] _commit = new Commits[1];
			_commit[0] = new Commits(date, num);
			if (numCommits == null) {
				numCommits = new Commits[1];
				System.arraycopy(_commit, 0, numCommits, 0, 1);
				return;
			}
			this.addCommits(_commit[0]);
		}
	}

	public int index(Date date) {
		if (numCommits == null)
			return -1;
		return IntStream.range(0, numCommits.length).filter(i -> numCommits[i].getDate() == date).findFirst()
				.orElse(-1);
	}

	public int index(String date) {
		Date tmp;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		try {
			tmp = formatter.parse(date);
			if (numCommits == null)
				return -1;
			int index = IntStream.range(0, numCommits.length).filter(i -> numCommits[i].getDate().compareTo(tmp) == 0)
					.findFirst().orElse(-1);
			return index;
		} catch (ParseException e) {
			System.out.println("Parse Date error");
			// e.printStackTrace();
			return -1;
		}
	}

	public void addMembers(long[] ids) {
		long[] _memberId = this.memberId;
		memberId = new long[_memberId.length + ids.length];
		System.arraycopy(_memberId, 0, memberId, 0, _memberId.length);
		System.arraycopy(ids, 0, memberId, _memberId.length, ids.length);
	}

	public void addMember(long id) {
		if (isMember(id)) {
			return;
		}
		if (memberId == null) {
			memberId = new long[1];
			memberId[0] = id;
			return;
		}
		long[] _memberId = this.memberId;
		memberId = new long[_memberId.length + 1];
		System.arraycopy(_memberId, 0, memberId, 0, _memberId.length);
		memberId[memberId.length - 1] = id;
	}

	public void addIssue(Issue _iss) {
		if (issue == null) {
			issue = new Issue[1];
			issue[0] = new Issue(_iss.getId(), _iss.getStartTime());
			return;
		}
		Issue[] _tmp = new Issue[1];
		_tmp[0] = _iss;
		Issue[] _issue = this.issue;
		this.issue = new Issue[_issue.length + 1];
		System.arraycopy(_issue, 0, this.issue, 0, _issue.length);
		System.arraycopy(_tmp, 0, this.issue, _issue.length, 1);
	}

	public void addIssue(long id, String startTime) {
		if (issue == null) {
			issue = new Issue[1];
			issue[0] = new Issue(id, startTime);
			return;
		}
		Issue[] _tmp = new Issue[1];
		_tmp[0] = new Issue(id, startTime);
		Issue[] _issue = this.issue;
		this.issue = new Issue[_issue.length + 1];
		System.arraycopy(_issue, 0, this.issue, 0, _issue.length);
		System.arraycopy(_tmp, 0, this.issue, _issue.length, 1);
	}

	public void updateIssue(long id, String endTime) {
		int index = indexIssue(id);
		if (index >= 0) {
			issue[index].setEndTime(endTime);
		}
	}

	public void updateIssue(Issue _issue) {
		int index = indexIssue(id);
		if (index >= 0) {
			issue[index].setEndTime(_issue.getEndTime());
		} else {
			addIssue(_issue);
		}
	}

	public void addPullRequest(PullRequest _pull) {
		PullRequest[] _tmp = new PullRequest[1];
		_tmp[0] = _pull;
		PullRequest[] _pullRequest = this.pullRequest;
		this.pullRequest = new PullRequest[_pullRequest.length + 1];
		System.arraycopy(_pullRequest, 0, this.pullRequest, 0, _pullRequest.length);
		System.arraycopy(_tmp, 0, this.pullRequest, _pullRequest.length, 1);
	}

	public void addPullRequest(long id, String startTime) {
		if (pullRequest == null) {
			pullRequest = new PullRequest[1];
			pullRequest[0] = new PullRequest(id, startTime);
			return;
		}
		PullRequest[] _tmp = new PullRequest[1];
		_tmp[0] = new PullRequest(id, startTime);
		PullRequest[] _pullRequest = this.pullRequest;
		this.pullRequest = new PullRequest[_pullRequest.length + 1];
		System.arraycopy(_pullRequest, 0, this.pullRequest, 0, _pullRequest.length);
		System.arraycopy(_tmp, 0, this.pullRequest, _pullRequest.length, 1);
	}

	public void updatePullRequest(long id, String endTime) {
		int index = indexPullRequest(id);
		if (index >= 0) {
			pullRequest[index].setEndTime(endTime);
		}
	}

	public void updatePullRequest(PullRequest _pull) {
		int index = indexPullRequest(id);
		if (index >= 0) {
			pullRequest[index].setEndTime(_pull.getEndTime());
		} else {
			addPullRequest(_pull);
		}
	}

	public boolean isMember(long id) {
		if (memberId == null)
			return false;
		return Arrays.stream(memberId).anyMatch(i -> i == id);
	}

	public int indexIssue(long id) {
		if (issue == null) {
			return -1;
		}
		return IntStream.range(0, issue.length).filter(i -> issue[i].getId() == id).findFirst().orElse(-1);
	}

	public boolean isIssue(long id) {
		if (issue == null)
			return false;
		return Arrays.stream(issue).anyMatch(i -> i.getId() == id);
	}

	public int indexPullRequest(long id) {
		if (issue == null) {
			return -1;
		}
		return IntStream.range(0, pullRequest.length).filter(i -> issue[i].getId() == id).findFirst().orElse(-1);
	}

	public boolean isPullRequest(long id) {
		if (issue == null)
			return false;
		return Arrays.stream(pullRequest).anyMatch(i -> i.getId() == id);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long totalCommit() {
		long _tt = 0;
		if (numCommits != null) {
			for (int i = 0; i < numCommits.length; i++) {
				_tt += numCommits[i].getNumCommits();
			}
			setTotalCommit(_tt);
			return _tt;
		}
		setTotalCommit(0);
		return 0;
	}

	public int getNumCommitsperDay() {
		if (numCommits != null)
			return (int) getTotalCommit() / numCommits.length;
		else
			return 0;
	}

	public int getRatioCommitsperMenmBer() {
		return ratioCommitsperMenmBer;
	}

	public void setRatioCommitsperMenmBer(int ratioCommitsperMenmBer) {
		this.ratioCommitsperMenmBer = ratioCommitsperMenmBer;
	}

	public int getNumContributors() {
		if (numCommits != null)
			return numCommits.length;
		else
			return 0;
	}

	public Commits[] getNumCommits() {
		return numCommits;
	}

	public long averageTimeIssue() {
		long _tt = 0;
		if (issue != null) {
			for (int i = 0; i < issue.length; i++) {
				if ((issue[i].getStartTime() != null) && (issue[i].getEndTime() != null))
					_tt = (issue[i].getEndTime().getTime() - issue[i].getStartTime().getTime()) / 3600000;
				else if ((issue[i].getStartTime() != null) && (issue[i].getEndTime() == null)) {
					_tt = ((new Date()).getTime() - issue[i].getStartTime().getTime()) / 3600000;
				}
			}
			return _tt / issue.length;
		}
		return 0;
	}

	public long averageTimeRQ2Merge() {
		long _tt = 0;
		if (pullRequest != null) {
			for (int i = 0; i < pullRequest.length; i++) {
				if ((pullRequest[i].getStartTime() != null) && (pullRequest[i].getEndTime() != null))
					_tt = (pullRequest[i].getEndTime().getTime() - pullRequest[i].getStartTime().getTime()) / 3600000;
				else if ((pullRequest[i].getStartTime() != null) && (pullRequest[i].getEndTime() == null))
					_tt = ((new Date()).getTime() - pullRequest[i].getStartTime().getTime()) / 3600000;
			}
			return _tt / pullRequest.length;
		}
		return 0;
	}

	public float commitPerDev() {
		return getTotalCommit() / memberId.length;
	}

}
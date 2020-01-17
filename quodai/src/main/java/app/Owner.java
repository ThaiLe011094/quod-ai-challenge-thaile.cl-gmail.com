package app;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Owner {
	public Repo[] repo;
	public String nameOwner;
	public long idOwner;

	public Owner() {

	}

	public synchronized void updateOwner(Owner own) {
		int m = own.repoLength();
		int n = this.repoLength();
		int start = 0;
		int end = 0;
		if (this.repo == null) {
			addRepo(own.repo);
			return;
		}
		for (int i = 0; i < m; i++) {
			boolean flag = false;
			int index = indexRepo(own.repo[i].getId());
			if (index >= 0) {
				if (start < end) {
					Repo[] _tmp = new Repo[end - start + 1];
					System.arraycopy(own.repo, start, _tmp, 0, end - start + 1);
					addRepo(_tmp);
				}
				start = i + 1;
				end = i + 1;
				this.repo[index].updateParams(own.repo[i]);
			} else {
				end++;
			}
		}
	}

	public void addRepo(Repo[] _repo) {
		if (repo == null) {
			this.repo = new Repo[_repo.length];
			System.arraycopy(_repo, 0, this.repo, 0, _repo.length);
		} else {
			Repo[] _tmp = this.repo;
			this.repo = new Repo[_repo.length + _tmp.length];
			System.arraycopy(_tmp, 0, this.repo, 0, _tmp.length);
			System.arraycopy(_repo, 0, this.repo, _tmp.length, _repo.length);
		}
	}

	public void addRepo(Repo repo) {
		Repo[] _tmp = new Repo[1];
		Repo[] _repo = this.repo;
		_tmp[0] = repo;
		this.repo = new Repo[_repo.length + _tmp.length];
		System.arraycopy(_repo, 0, this.repo, 0, _repo.length);
		System.arraycopy(_tmp, 0, this.repo, _repo.length, 1);
	}

	public void addRepo(long id, String name) {
		Repo[] _tmp = new Repo[1];
		_tmp[0] = new Repo(id, name);
		if (repo == null) {
			this.repo = new Repo[1];
			System.arraycopy(_tmp, 0, repo, 0, 1);
			return;
		}
		if (isExistRepo(id)) {
			return;
		}
		Repo[] _repo = this.repo;
		this.repo = new Repo[_repo.length + _tmp.length];
		System.arraycopy(_repo, 0, this.repo, 0, _repo.length);
		System.arraycopy(_tmp, 0, this.repo, _repo.length, 1);
	}

	public void updateRepo(long id, String name, String date, int numCommits) {
		int index = indexRepo(id);
		if (index >= 0) {
			repo[index].updateCommits(date, numCommits);
		} else {
			addRepo(id, name);
		}

	}

	public boolean isContainRepo(long id) {
		return Arrays.stream(repo).allMatch(i -> i.getId() == id);
	}

	public void addContributor(long idRepo, long idActor) {
		int index = indexRepo(idRepo);
		if (index >= 0) {
			repo[index].addMember(idActor);
		} else {
			addRepo(idRepo, null);
		}

	}

	public boolean isExistRepo(long id) {
		if (repo[0] == null)
			return false;
		return Arrays.stream(repo).anyMatch(i -> i.getId() == id);
	}

	public int indexRepo(long id) {
		if (repo == null)
			return -1;
		return IntStream.range(0, repo.length).filter(i -> repo[i].getId() == id).findFirst().orElse(-1);
	}

	public void updateIssue(long idRepo, long idIssue, String startTime, String endTime) {
		int index = indexRepo(idRepo);
		if (index >= 0) {
			if (startTime != null) {
				repo[index].addIssue(idIssue, startTime);
			} else if (endTime != null) {
				repo[index].updateIssue(idIssue, endTime);
			}

		} else {
			addRepo(idRepo, null);
		}
	}

	public void updatePullRequest(long idRepo, long idRQ, String startTime, String endTime) {
		int index = indexRepo(idRepo);
		if (index >= 0) {
			if (startTime != null) {
				repo[index].addIssue(idRQ, startTime);
			} else if (endTime != null) {
				repo[index].updateIssue(idRQ, endTime);
			}

		} else {
			addRepo(idRepo, null);
		}
	}

	public int repoLength() {
		if (repo == null)
			return 0;
		return repo.length;
	}

}
package app;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
	public static final int MAX_THREAD = 10;

	public static void main(String[] args) throws Exception {
		DataPrepair pre = new DataPrepair();
		RunApp run = new RunApp();
		pre = run.argFunc(args);

		List<String> result = null;
		run.download(pre.startDate, pre.endDate);
		try (Stream<Path> walk = Files.walk(Paths.get("./data"))) {
			result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
			int size = result.size();
			int n = size;
			ExecutorService es = Executors.newCachedThreadPool();
			// Locking thread here for not getting overlapped while a thread is loading data

			while (n > 0) {
				for (int i = 0; i < MAX_THREAD; i++) {
					if (n > 0) {
						RunApp _tmp = new RunApp();
						ReadMultiple thread = new ReadMultiple(result.remove(n - 1), _tmp, run);
						es.execute(thread);
					}
					n = result.size();
				}
			}
			es.shutdown();
			boolean finshed = es.awaitTermination(1, TimeUnit.MINUTES);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		String dateString = dtf.format(now);
		ExecutorService es = Executors.newCachedThreadPool();

		// Getting OS temp directory
		String property = "java.io.tmpdir";
		String tempDir = System.getProperty(property);

		for (int i = 0; i < MAX_THREAD; i++) {
			int epoch = (int) (run.owner.repo.length / MAX_THREAD);
			Repo[] _tmp = new Repo[epoch];
			System.arraycopy(run.owner.repo, i * MAX_THREAD, _tmp, 0, epoch);
			WriteMultiple thread = new WriteMultiple(tempDir + "/report-" + dateString + "-" + i + ".csv", _tmp);
			// temp files would stay in OS Temp Dir and will be flushed away by OS Schedule
			es.execute(thread);
		}
		es.shutdown();
		boolean finshed = es.awaitTermination(1, TimeUnit.MINUTES);
		
		// Merging due to splitted csv during multi-thread writing
		List<Path> paths = Arrays.asList(Paths.get(tempDir + "/report-" + dateString + "-0" + ".csv"),
				Paths.get(tempDir + "/report-" + dateString + "-1" + ".csv"),
				Paths.get(tempDir + "/report-" + dateString + "-2" + ".csv"),
				Paths.get(tempDir + "/report-" + dateString + "-3" + ".csv"),
				Paths.get(tempDir + "/report-" + dateString + "-4" + ".csv"),
				Paths.get(tempDir + "/report-" + dateString + "-5" + ".csv"),
				Paths.get(tempDir + "/report-" + dateString + "-6" + ".csv"),
				Paths.get(tempDir + "/report-" + dateString + "-7" + ".csv"),
				Paths.get(tempDir + "/report-" + dateString + "-8" + ".csv"),
				Paths.get(tempDir + "/report-" + dateString + "-9" + ".csv"));
		List<String> mergedLines = Merge.getMergedLines(paths);
		if (new File("./report/").exists()) {

		} else {
			try {
				File file = new File("./report/");
				file.mkdir();
			} catch (Exception e) {
				System.out.println("Could not create report folder");
			}
		}
		Path target = Paths.get("./report/report-" + dateString + ".csv");

		try {
			Files.write(target, mergedLines, Charset.forName("UTF-8"));

			File file = new File("./report/report-" + dateString + ".csv");
			String path = file.getAbsolutePath().replace("\\.", "");

			System.out.println("Reported at " + path);
			System.out.println("Job done");
		} catch (Exception e) {
			System.out.println("Could not write the report ");
		}

	}

}

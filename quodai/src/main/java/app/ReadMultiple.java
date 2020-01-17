package app;

import java.util.concurrent.Semaphore;

class ReadMultiple extends Thread {
	static Semaphore semaphore = new Semaphore(App.MAX_THREAD); // Max semaphore :10
	private String data;
	private RunApp app;
	private RunApp mainApp;

	public ReadMultiple(String data, RunApp app, RunApp mApp) {
		this.app = app;
		this.data = data;
		this.mainApp = mApp;
	}

	@Override
	public void run() {
		try {
			semaphore.acquire();
			// Displaying the thread that is running
			System.out.println("Thread " + Thread.currentThread().getId() + " is running");
			app.readData2CSV(data);
			mainApp.update(app.owner);
			semaphore.release();
		} catch (Exception e) {
			// Throwing an exception
			System.out.println("Exception is caught in Read");
		}
	}
}
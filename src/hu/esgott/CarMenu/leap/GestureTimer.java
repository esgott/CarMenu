package hu.esgott.CarMenu.leap;

import java.util.Timer;
import java.util.TimerTask;

public class GestureTimer {

	private int frameUntilCancel;
	private int frameLeft;
	private long timeUntilStart;
	private Timer timer = new Timer();
	private TimerTask timerTask;
	private boolean running = false;
	private Runnable onStart;
	private Runnable onStop;

	public GestureTimer(float secondsUntilStart, int frameUntilCancel,
			TimerTask task, Runnable onStart, Runnable onStop) {
		this.frameUntilCancel = frameUntilCancel;
		frameLeft = frameUntilCancel;
		timeUntilStart = Math.round(secondsUntilStart * 1000);
		timerTask = task;
		this.onStart = onStart;
		this.onStop = onStop;
	}

	public void start() {
		if (!running) {
			timer.schedule(timerTask, timeUntilStart);
			running = true;
			if (onStart != null) {
				onStart.run();
			}
		}
		frameLeft = frameUntilCancel;
	}

	public void stop() {
		if (running && (frameLeft == 0)) {
			timerTask.cancel();
			running = false;
			if (onStop != null) {
				onStop.run();
			}
		}
		if (frameUntilCancel > 0) {
			frameUntilCancel--;
		}
	}

}

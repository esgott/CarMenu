package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.menu.StatusBar;
import hu.esgott.CarMenu.sound.Recorder;

public class SpeechTimer {

	private GestureTimer gestureTimer;
	private Recorder recorder = new Recorder();

	public SpeechTimer(final StatusBar statusBar) {

		Runnable task = new Runnable() {
			@Override
			public void run() {
				statusBar.setRecordMode(true);
				recorder.record();
			}
		};

		Runnable onStart = new Runnable() {
			@Override
			public void run() {
			}
		};

		Runnable onStop = new Runnable() {
			@Override
			public void run() {
				recorder.stop();
				statusBar.setRecordMode(false);
			}
		};

		gestureTimer = new GestureTimer(1.5f, 50, task, onStart, onStop);
	}

	public void recordGestureStarted() {
		gestureTimer.start();
	}

	public void recordGestureFinished() {
		gestureTimer.stop();
	}

	public void dispose() {
		gestureTimer.dispose();
		recorder.dispose();
	}

}

package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.menu.StatusBar;

public class SpeechTimer {

	private GestureTimer gestureTimer;

	public SpeechTimer(final StatusBar statusBar) {

		Runnable task = new Runnable() {
			@Override
			public void run() {
			}
		};

		Runnable onStart = new Runnable() {
			@Override
			public void run() {
				statusBar.setRecordMode(true);
			}
		};

		Runnable onStop = new Runnable() {
			@Override
			public void run() {
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
	}

}

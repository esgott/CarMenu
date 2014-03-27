package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.sound.Recorder;

public class SpeechTimer {

	private GestureTimer gestureTimer;

	public SpeechTimer(final Recorder recorder) {

		Runnable task = new Runnable() {
			@Override
			public void run() {
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

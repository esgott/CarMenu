package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;

import java.util.TimerTask;

public class ExitTimer {

	private GestureTimer gestureTimer;

	public ExitTimer(final MenuList menuList, final StatusBar statusBar) {

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				menuList.exit();
			}
		};

		Runnable onStart = new Runnable() {
			@Override
			public void run() {
				statusBar.setExitMode(true);
			}
		};

		Runnable onStop = new Runnable() {
			@Override
			public void run() {
				statusBar.setExitMode(false);
			}
		};

		gestureTimer = new GestureTimer(1, 10, timerTask, onStart, onStop);
	}

	public void exitSituation() {
		gestureTimer.start();
	}

	public void notExitSituation() {
		gestureTimer.stop();
	}

}

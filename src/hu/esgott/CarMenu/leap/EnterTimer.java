package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;

import java.util.TimerTask;

public class EnterTimer {

	private GestureTimer gestureTimer;

	public EnterTimer(final MenuList menuList, final StatusBar statusBar) {

		Runnable task = new TimerTask() {
			@Override
			public void run() {
				menuList.enter();
			}
		};

		Runnable onStart = new Runnable() {
			@Override
			public void run() {
				statusBar.setEnterMode(true);
			}
		};

		Runnable onStop = new Runnable() {
			@Override
			public void run() {
				statusBar.setEnterMode(false);
			}
		};

		gestureTimer = new GestureTimer(1, 30, task, onStart, onStop);
	}

	public void enterSituation() {
		gestureTimer.start();
	}

	public void notEnterSituation() {
		gestureTimer.stop();
	}

	public void dispose() {
		gestureTimer.dispose();
	}

}

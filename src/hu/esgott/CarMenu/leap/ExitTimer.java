package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.menu.MenuList;

import java.util.Timer;
import java.util.TimerTask;

public class ExitTimer {

	private MenuList menuList;
	private Timer timer = new Timer();
	private TimerTask timerTask;
	private boolean running = false;
	private static final int ONE_SECOND = 1000;

	public ExitTimer(MenuList menuList) {
		this.menuList = menuList;
	}

	public void exitSituation() {
		if (!running) {
			timerTask = new TimerTask() {
				@Override
				public void run() {
					menuList.exit();
				}
			};
			timer.schedule(timerTask, ONE_SECOND);
			running = true;
		}
	}

	public void notExitSituation() {
		if (running) {
			timerTask.cancel();
			running = false;
		}
	}

}

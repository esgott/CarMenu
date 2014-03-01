package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;

import java.util.Timer;
import java.util.TimerTask;

public class ExitTimer {

	private MenuList menuList;
	private StatusBar statusBar;
	private Timer timer = new Timer();
	private TimerTask timerTask;
	private boolean running = false;
	private int frameUntilCancel;
	private static final int ONE_SECOND = 1000;
	private static final int MIN_FRAME_UNTIL_CANCEL = 10;

	public ExitTimer(MenuList menuList, StatusBar statusBar) {
		this.menuList = menuList;
		this.statusBar = statusBar;
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
			statusBar.setExitMode(true);
		}
		frameUntilCancel = MIN_FRAME_UNTIL_CANCEL;
	}

	public void notExitSituation() {
		if (running && (frameUntilCancel == 0)) {
			timerTask.cancel();
			running = false;
			statusBar.setExitMode(false);
		}
		frameUntilCancel--;
	}

}

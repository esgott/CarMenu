package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.MainWindow;
import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;

import com.leapmotion.leap.Config;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;

public class LeapListener extends Listener {

	private MenuList menuList;
	private StatusBar statusBar;
	private ExitTimer exitTimer;
	private EnterTimer enterTimer;
	private SpeechTimer speechTimer;
	private int previousSwipeGestureId;
	private int frameUntilNextSwipe = 0;
	private boolean swipeProcessed;
	private static final int MAX_SWYPE_FREQ = 10;
	private static final float MIN_SWIPE_LENGTH = 200.0f;
	private static final float SPEED_LIMIT = 20.0f;

	public LeapListener(MainWindow mainWindow) {
		menuList = mainWindow.getMenuList();
		statusBar = mainWindow.getStatusBar();
		exitTimer = new ExitTimer(menuList, statusBar);
		enterTimer = new EnterTimer(menuList, statusBar);
		speechTimer = new SpeechTimer(mainWindow.getRecorder());
	}

	@Override
	public void onConnect(Controller controller) {
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		Config config = controller.config();
		if (config.setFloat("Gesture.Swipe.MinLength", MIN_SWIPE_LENGTH)) {
			config.save();
		}
		statusBar.setLeapConnected(true);
	}

	@Override
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();
		swipeProcessed = false;
		if (frameUntilNextSwipe > 0) {
			frameUntilNextSwipe--;
		}
		GestureList gestures = frame.gestures();
		for (Gesture gesture : gestures) {
			handleGesture(gesture);
		}
		checkIfExit(frame);
	}

	private void handleGesture(Gesture gesture) {
		switch (gesture.type()) {
		case TYPE_SWIPE:
			handleSwipeGesture(gesture);
			break;
		default:
			break;
		}
	}

	private void handleSwipeGesture(Gesture gesture) {
		SwipeGesture swipeGesture = new SwipeGesture(gesture);
		if ((previousSwipeGestureId != swipeGesture.id()) && (!swipeProcessed)
				&& (frameUntilNextSwipe == 0)) {
			Vector direction = swipeGesture.direction();
			if (direction.getX() > 0) {
				menuList.next();
			} else {
				menuList.next();
			}
			previousSwipeGestureId = swipeGesture.id();
			swipeProcessed = true;
		}
		frameUntilNextSwipe = MAX_SWYPE_FREQ;
	}

	private void checkIfExit(Frame frame) {
		int handsCount = frame.hands().count();
		if ((handsCount == 1)) {
			Hand hand = frame.hands().get(0);
			float speed = hand.palmVelocity().magnitude();
			int fingerCount = frame.fingers().count();
			if (speed < SPEED_LIMIT) {
				enableActionOnFingerNum(fingerCount);
				return;
			}
		}
		disableActions();
	}

	private void enableActionOnFingerNum(int fingerNum) {
		if (fingerNum <= 1) {
			speechTimer.recordGestureStarted();
		} else if (fingerNum == 2) {
			enterTimer.enterSituation();
		} else if (fingerNum == 5) {
			exitTimer.exitSituation();
		}
	}

	private void disableActions() {
		exitTimer.notExitSituation();
		enterTimer.notEnterSituation();
		speechTimer.recordGestureFinished();
	}

	@Override
	public void onDisconnect(Controller arg0) {
		statusBar.setLeapConnected(false);
	}

	public void dispose() {
		exitTimer.dispose();
		enterTimer.dispose();
		speechTimer.dispose();
	}

}

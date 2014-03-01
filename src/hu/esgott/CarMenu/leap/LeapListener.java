package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.menu.MenuList;

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
	private ExitTimer exitTimer;
	private int previousSwipeGestureId;
	private static final float SPEED_LIMIT = 10.0f;

	public LeapListener(MenuList menuList) {
		this.menuList = menuList;
		exitTimer = new ExitTimer(menuList);
	}

	@Override
	public void onConnect(Controller controller) {
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
	}

	@Override
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();
		GestureList gestures = frame.gestures();
		for (Gesture gesture : gestures) {
			handleGesture(gesture);
		}
		checkIfExit(frame);
	}

	private void handleGesture(Gesture gesture) {
		switch (gesture.type()) {
		case TYPE_SCREEN_TAP:
			handleTapGesture();
			break;
		case TYPE_SWIPE:
			handleSwipeGesture(gesture);
			break;
		default:
			break;
		}
	}

	private void handleTapGesture() {
		menuList.enter();
	}

	private void handleSwipeGesture(Gesture gesture) {
		SwipeGesture swipeGesture = new SwipeGesture(gesture);
		if (previousSwipeGestureId != swipeGesture.id()) {
			Vector direction = swipeGesture.direction();
			if (direction.getX() > 0) {
				menuList.next();
			} else {
				menuList.previous();
			}
			previousSwipeGestureId = swipeGesture.id();
		}
	}

	private void checkIfExit(Frame frame) {
		int handsCount = frame.hands().count();
		int fingerCount = frame.fingers().count();
		if ((handsCount == 1) && (fingerCount == 5)) {
			Hand hand = frame.hands().get(0);
			float speed = hand.palmVelocity().magnitude();
			if (speed < SPEED_LIMIT) {
				exitTimer.exitSituation();
			} else {
				exitTimer.notExitSituation();
			}
		}
	}

}

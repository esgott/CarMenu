package hu.esgott.CarMenu.leap;

import hu.esgott.CarMenu.menu.MenuList;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

public class LeapListener extends Listener {

	private MenuList menuList;

	public LeapListener(MenuList menuList) {
		this.menuList = menuList;
	}

	@Override
	public void onFrame(Controller controller) {
	}

}

package hu.esgott.CarMenu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParkingRadarMenuBuilder implements MenuBuilder {

	private Menu settingsMenu;

	public ParkingRadarMenuBuilder(Menu settingsMenu) {
		this.settingsMenu = settingsMenu;
	}

	@Override
	public ObservableList<MenuElement> build(Menu caller) {
		ObservableList<MenuElement> list = FXCollections.observableArrayList();
		list.add(new MenuElement("Bekapcsolva", caller, null, "bekapcsolva"));
		list.add(new MenuElement("Kikapcsolva", caller, null, "kikapcsolva"));
		return list;
	}

	@Override
	public Menu upper() {
		return settingsMenu;
	}

}

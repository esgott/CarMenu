package hu.esgott.CarMenu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SettingsMenuBuilder implements MenuBuilder {

	private Menu mainMenu;
	private Menu parkingRadarMenu;

	public SettingsMenuBuilder(Menu mainMenu, Menu parkingRadarMenu) {
		this.mainMenu = mainMenu;
		this.parkingRadarMenu = parkingRadarMenu;
	}

	@Override
	public ObservableList<MenuElement> build(Menu caller) {
		ObservableList<MenuElement> list = FXCollections.observableArrayList();
		list.add(new MenuElement("Parkolóradar", caller, parkingRadarMenu, "parkoloradar"));
		return list;
	}

	@Override
	public Menu upper() {
		return mainMenu;
	}

}

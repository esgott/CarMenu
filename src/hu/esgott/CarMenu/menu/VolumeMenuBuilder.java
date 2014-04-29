package hu.esgott.CarMenu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VolumeMenuBuilder implements MenuBuilder {

	private Menu mainMenu;

	public VolumeMenuBuilder(Menu mainMenu) {
		this.mainMenu = mainMenu;
	}

	@Override
	public ObservableList<MenuElement> build(Menu caller) {
		ObservableList<MenuElement> list = FXCollections.observableArrayList();
		for (Integer i = 1; i <= 30; i++) {
			if (i == 10) {
				list.add(new MenuElement(i.toString(), caller, null, "normal"));
			} else {
				list.add(new MenuElement(i.toString(), caller, null));
			}
		}
		return list;
	}

	@Override
	public Menu upper() {
		return mainMenu;
	}

}

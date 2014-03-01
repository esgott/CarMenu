package hu.esgott.CarMenu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VentilationMenuBuilder implements MenuBuilder {

	private Menu mainMenu;

	public VentilationMenuBuilder(Menu mainMenu) {
		this.mainMenu = mainMenu;
	}

	@Override
	public ObservableList<MenuElement> build() {
		ObservableList<MenuElement> list = FXCollections.observableArrayList();
		list.add(new MenuElement("Alacsony", null));
		list.add(new MenuElement("Közepes", null));
		list.add(new MenuElement("Magas", null));
		return list;
	}

	@Override
	public Menu upper() {
		return mainMenu;
	}

}

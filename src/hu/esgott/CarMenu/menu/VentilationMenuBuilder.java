package hu.esgott.CarMenu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VentilationMenuBuilder implements MenuBuilder {

	private Menu mainMenu;

	public VentilationMenuBuilder(Menu mainMenu) {
		this.mainMenu = mainMenu;
	}

	@Override
	public ObservableList<MenuElement> build(Menu caller) {
		ObservableList<MenuElement> list = FXCollections.observableArrayList();
		list.add(new MenuElement("Kikapcsolva", caller, null, "kikapcsolva"));
		list.add(new MenuElement("Alacsony", caller, null, "alacsony"));
		list.add(new MenuElement("Közepes", caller, null, "kozepes"));
		list.add(new MenuElement("Magas", caller, null, "magas"));
		return list;
	}

	@Override
	public Menu upper() {
		return mainMenu;
	}

}

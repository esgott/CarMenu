package hu.esgott.CarMenu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainMenuBuilder implements MenuBuilder {

	private Menu ventilationMenu;

	public MainMenuBuilder(Menu ventilationMenu) {
		this.ventilationMenu = ventilationMenu;
	}

	@Override
	public ObservableList<MenuElement> build() {
		ObservableList<MenuElement> list = FXCollections.observableArrayList();
		list.add(new MenuElement("Szellőzés", ventilationMenu));
		list.add(new MenuElement("Hangerő", null));
		return list;
	}

	@Override
	public Menu upper() {
		return null;
	}

}

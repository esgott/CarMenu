package hu.esgott.CarMenu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainMenuBuilder implements MenuBuilder {

	private Menu ventilationMenu;
	private Menu volumeMenu;

	public MainMenuBuilder(Menu ventilationMenu, Menu volumeMenu) {
		this.ventilationMenu = ventilationMenu;
		this.volumeMenu = volumeMenu;
	}

	@Override
	public ObservableList<MenuElement> build(Menu caller) {
		ObservableList<MenuElement> list = FXCollections.observableArrayList();
		list.add(new MenuElement("Szellőzés", caller, ventilationMenu));
		list.add(new MenuElement("Hangerő", caller, volumeMenu));
		return list;
	}

	@Override
	public Menu upper() {
		return null;
	}

}

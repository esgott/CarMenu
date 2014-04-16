package hu.esgott.CarMenu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainMenuBuilder implements MenuBuilder {

	private Menu ventilationMenu;
	private Menu volumeMenu;
	private Menu settingsMenu;

	public MainMenuBuilder(Menu ventilationMenu, Menu volumeMenu,
			Menu settingsMenu) {
		this.ventilationMenu = ventilationMenu;
		this.volumeMenu = volumeMenu;
		this.settingsMenu = settingsMenu;
	}

	@Override
	public ObservableList<MenuElement> build(Menu caller) {
		ObservableList<MenuElement> list = FXCollections.observableArrayList();
		list.add(new MenuElement("Szellőzés", caller, ventilationMenu, "szellozes"));
		list.add(new MenuElement("Hangerő", caller, volumeMenu, "hangero"));
		list.add(new MenuElement("Beállítások", caller, settingsMenu));
		return list;
	}

	@Override
	public Menu upper() {
		return null;
	}

}

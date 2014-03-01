package hu.esgott.CarMenu.menu;

import javafx.collections.ObservableList;

public class Menu {

	private ObservableList<MenuElement> menuElements;
	private Menu upper;
	private int position;

	public void fill(MenuBuilder builder) {
		menuElements = builder.build();
		upper = builder.upper();
	}

	public ObservableList<MenuElement> getContent() {
		return menuElements;
	}

	public Menu getParentMenu() {
		return upper;
	}

	public void setPosition(int pos) {
		position = pos;
	}

	public int getPosition() {
		return position;
	}

}

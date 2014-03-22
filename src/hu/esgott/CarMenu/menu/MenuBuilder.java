package hu.esgott.CarMenu.menu;

import javafx.collections.ObservableList;

interface MenuBuilder {

	public ObservableList<MenuElement> build(Menu caller);

	public Menu upper();

}

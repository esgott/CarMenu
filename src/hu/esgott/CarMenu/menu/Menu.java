package hu.esgott.CarMenu.menu;

import javafx.collections.ObservableList;

public class Menu {

	private ObservableList<MenuElement> menuElements;
	private MenuElement selectedOption;
	private Menu upper;
	private int position;

	public void fill(MenuBuilder builder) {
		menuElements = builder.build(this);
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

	public void select(MenuElement element) {
		selectedOption = element;
	}

	public MenuElement getSelectedOption() {
		return selectedOption;
	}

	public int getSelectedOptionIndex() {
		if (selectedOption == null) {
			return 0;
		} else {
			return menuElements.indexOf(selectedOption);
		}
	}

	public MenuElement menuForSpeech(String pattern) {
		for (MenuElement element : menuElements) {
			if (element.matches(pattern)) {
				return element;
			}
		}
		return null;
	}
}

package hu.esgott.CarMenu.menu;

public class MenuElement {

	public String name;
	public Menu next;

	public MenuElement(String name, Menu next) {
		this.name = name;
		this.next = next;
	}

	@Override
	public String toString() {
		return name;
	}

	public void action() {
	}

}

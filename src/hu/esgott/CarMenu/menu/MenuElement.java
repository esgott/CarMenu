package hu.esgott.CarMenu.menu;

public class MenuElement {

	public String name;
	public Menu child;
	public Menu parent;

	public MenuElement(String name, Menu parent, Menu child) {
		this.name = name;
		this.child = child;
		this.parent = parent;
	}

	@Override
	public String toString() {
		return name;
	}

	public void action() {
		parent.select(this);
	}

}

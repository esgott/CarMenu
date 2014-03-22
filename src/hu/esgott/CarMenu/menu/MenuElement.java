package hu.esgott.CarMenu.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuElement {

	private String name;
	private Menu child;
	private Menu parent;
	private List<String> speechTerms;

	public MenuElement(String name, Menu parent, Menu child, String... speech) {
		this.name = name;
		this.child = child;
		this.parent = parent;
		speechTerms = new ArrayList<>(Arrays.asList(speech));
	}

	@Override
	public String toString() {
		return name;
	}

	public Menu getChild() {
		return child;
	}

	public void action() {
		parent.select(this);
	}

	public boolean matches(String recognizedPattern) {
		return speechTerms.contains(recognizedPattern);
	}

}

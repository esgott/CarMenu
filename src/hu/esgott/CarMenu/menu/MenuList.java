package hu.esgott.CarMenu.menu;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;

public class MenuList {

	private ListView<MenuElement> listView = new ListView<>();
	private SelectionModel<MenuElement> selectionModel = listView
			.getSelectionModel();
	private Menu mainMenu = new Menu();
	private Menu ventilationMenu = new Menu();
	private Menu currentMenu = mainMenu;

	public MenuList() {
		createMenus();
		listView.setItems(mainMenu.getContent());
		selectFirst();
	}

	private void selectFirst() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				selectionModel.selectFirst();
			}
		});
	}

	private void createMenus() {
		mainMenu.fill(new MainMenuBuilder(ventilationMenu));
		ventilationMenu.fill(new VentilationMenuBuilder(mainMenu));
	}

	public void previous() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				selectionModel.selectPrevious();
			}
		});
	}

	public void next() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				selectionModel.selectNext();
			}
		});
	}

	public void enter() {
		MenuElement menuElement = selectionModel.getSelectedItem();
		if (menuElement != null) {
			enterLowerMenu(menuElement);
		}
	}

	private void enterLowerMenu(MenuElement menuElement) {
		Menu selectedMenu = menuElement.next;
		if (selectedMenu != null) {
			currentMenu.setPosition(selectionModel.getSelectedIndex());
			currentMenu = selectedMenu;
			listView.setItems(selectedMenu.getContent());
			selectFirst();
		}
	}

	public void exit() {
		Menu upperMenu = currentMenu.getParentMenu();
		if (upperMenu != null) {
			listView.setItems(upperMenu.getContent());
			select(upperMenu.getPosition());
			currentMenu = upperMenu;
		}
	}

	private void select(final int index) {
		if (!selectionModel.isSelected(index)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					selectionModel.select(index);
				}
			});
		}
	}

	public ListView<MenuElement> getList() {
		return listView;
	}

}

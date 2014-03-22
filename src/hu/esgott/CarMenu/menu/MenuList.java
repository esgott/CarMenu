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
	private Menu volumeMenu = new Menu();
	private Menu currentMenu = mainMenu;

	public MenuList() {
		createMenus();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				listView.setItems(mainMenu.getContent());
				selectionModel.selectFirst();
			}
		});
	}

	private void createMenus() {
		mainMenu.fill(new MainMenuBuilder(ventilationMenu, volumeMenu));
		ventilationMenu.fill(new VentilationMenuBuilder(mainMenu));
		volumeMenu.fill(new VolumeMenuBuilder(mainMenu));
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
		final Menu selectedMenu = menuElement.next;
		if (selectedMenu != null) {
			currentMenu.setPosition(selectionModel.getSelectedIndex());
			currentMenu = selectedMenu;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					listView.setItems(selectedMenu.getContent());
					selectionModel.selectFirst();
				}
			});
		}
	}

	public void exit() {
		final Menu upperMenu = currentMenu.getParentMenu();
		if (upperMenu != null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					listView.setItems(upperMenu.getContent());
					selectionModel.select(upperMenu.getPosition());
				}
			});
			currentMenu = upperMenu;
		}
	}

	public ListView<MenuElement> getList() {
		return listView;
	}

}

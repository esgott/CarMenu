package hu.esgott.CarMenu.menu;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;

public class MenuList {

	private ListView<MenuElement> listView = new ListView<>();
	private SelectionModel<MenuElement> selectionModel = listView
			.getSelectionModel();
	private Label selectionLabel;
	private Menu mainMenu = new Menu();
	private Menu ventilationMenu = new Menu();
	private Menu volumeMenu = new Menu();
	private Menu settingsMenu = new Menu();
	private Menu parkingRadarMenu = new Menu();
	private Menu currentMenu = mainMenu;

	public MenuList(Label selectionLabel) {
		createMenus();
		this.selectionLabel = selectionLabel;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				listView.setItems(mainMenu.getContent());
				selectionModel.selectFirst();
			}
		});
	}

	private void createMenus() {
		mainMenu.fill(new MainMenuBuilder(ventilationMenu, volumeMenu,
				settingsMenu));
		ventilationMenu.fill(new VentilationMenuBuilder(mainMenu));
		volumeMenu.fill(new VolumeMenuBuilder(mainMenu));
		settingsMenu.fill(new SettingsMenuBuilder(mainMenu, parkingRadarMenu));
		parkingRadarMenu.fill(new ParkingRadarMenuBuilder(settingsMenu));
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

	public void actionOnRecognizedString(String pattern) {
		System.out.println("Searching action for pattern: " + pattern);
		switch (pattern) {
		case "elozo":
			previous();
			break;
		case "kovetkezo":
			next();
			break;
		default:
			MenuElement matchingElement = currentMenu.menuForSpeech(pattern);
			if (matchingElement != null) {
				enterLowerMenu(matchingElement);
			}
		}
	}

	private void enterLowerMenu(MenuElement menuElement) {
		final Menu selectedMenu = menuElement.getChild();
		if (selectedMenu != null) {
			currentMenu.setPosition(selectionModel.getSelectedIndex());
			currentMenu = selectedMenu;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					listView.setItems(selectedMenu.getContent());
					int selectionIndex = currentMenu.getSelectedOptionIndex();
					selectionModel.select(selectionIndex);
				}
			});
		} else {
			menuElement.action();
			selectionLabel.setText(currentMenu.getSelectedOption().toString());
			exit();
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

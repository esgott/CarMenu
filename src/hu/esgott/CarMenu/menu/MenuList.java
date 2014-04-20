package hu.esgott.CarMenu.menu;

import java.awt.Toolkit;

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
		beep();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (selectionModel.getSelectedIndex() != 0) {
					selectionModel.selectPrevious();
				} else {
					selectionModel.selectLast();
				}
			}
		});
	}

	public void next() {
		beep();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (selectionModel.getSelectedIndex() != (currentMenu
						.getContent().size() - 1)) {
					selectionModel.selectNext();
				} else {
					selectionModel.selectFirst();
				}
			}
		});
	}

	public void enter() {
		beep();
		MenuElement menuElement = selectionModel.getSelectedItem();
		if (menuElement != null) {
			enterLowerMenuOrAction(menuElement);
		}
	}

	public void exit() {
		beep();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setUpperMenu(currentMenu.getParentMenu());
			}
		});
	}

	private void beep() {
		Toolkit.getDefaultToolkit().beep();
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
		case "kivalaszt":
			enter();
			break;
		case "vissza":
			exit();
			break;
		default:
			MenuElement matchingElement = currentMenu.menuForSpeech(pattern);
			if (matchingElement != null) {
				System.out.println("found " + matchingElement);
				enterLowerMenuOrAction(matchingElement);
			}
		}
	}

	private void enterLowerMenuOrAction(final MenuElement menuElement) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Menu lowerMenu = menuElement.getChild();
				if (lowerMenu != null) {
					currentMenu.setPosition(selectionModel.getSelectedIndex());
					currentMenu = lowerMenu;
					listView.setItems(lowerMenu.getContent());
					int selectionIndex = currentMenu.getSelectedOptionIndex();
					selectionModel.select(selectionIndex);
				} else {
					menuElement.action();
					MenuElement selectedMenu = currentMenu.getSelectedOption();
					selectionLabel.setText(selectedMenu.toString());
					setUpperMenu(currentMenu.getParentMenu());
				}
			}
		});
	}

	private void setUpperMenu(Menu upperMenu) {
		if (upperMenu != null) {
			listView.setItems(upperMenu.getContent());
			selectionModel.select(upperMenu.getPosition());
			currentMenu = upperMenu;
		}
	}

	public ListView<MenuElement> getList() {
		return listView;
	}

}

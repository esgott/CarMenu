package hu.esgott.CarMenu.menu;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class StatusBar {

	private HBox panel = new HBox();
	private ImageView leapImage = new ImageView(
			"hu/esgott/CarMenu/menu/icon/leap.png");
	private ImageView exitImage = new ImageView(
			"hu/esgott/CarMenu/menu/icon/exit.png");

	public StatusBar() {
		panel.getChildren().addAll(leapImage, exitImage);
		leapImage.setVisible(false);
		exitImage.setVisible(false);
	}

	public Pane getPanel() {
		return panel;
	}

	public void setLeapConnected(final boolean connected) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				leapImage.setVisible(connected);
			}
		});
	}

	public void setExitMode(final boolean exitMode) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				exitImage.setVisible(exitMode);
			}
		});
	}

}

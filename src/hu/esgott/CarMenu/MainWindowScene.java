package hu.esgott.CarMenu;

import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainWindowScene {

	private MainWindow mainWindow;
	private Scene scene;
	private StatusBar statusBar = new StatusBar();
	private Label selectionLabel = new Label();
	private MenuList menuList = new MenuList(selectionLabel);

	public MainWindowScene(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		scene = new Scene(createPane());
	}

	private Pane createPane() {
		BorderPane pane = new BorderPane();
		pane.setTop(statusBar.getPanel());
		pane.setCenter(menuList.getList());
		pane.setBottom(createBottom());
		return pane;
	}

	private Pane createBottom() {
		VBox bottomPane = new VBox();
		Pane buttonPane = createButtons();
		bottomPane.getChildren().addAll(selectionLabel, buttonPane);
		return bottomPane;
	}

	private Pane createButtons() {
		HBox buttonPane = new HBox();
		Button backButton = createBackButton();
		Button forwardButton = createForwardButton();
		Button enterButton = createEnterButton();
		Button exitButton = createExitButton();
		Button recordButton = createRecordButton();
		buttonPane.getChildren().addAll(backButton, forwardButton, enterButton,
				exitButton, recordButton);
		return buttonPane;
	}

	private Button createBackButton() {
		Button backButton = new Button("<-");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				menuList.previous();
			}
		});
		return backButton;
	}

	private Button createForwardButton() {
		Button forwardButton = new Button("->");
		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				menuList.next();
			}
		});
		return forwardButton;
	}

	private Button createEnterButton() {
		Button enterButton = new Button("Enter");
		enterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				menuList.enter();
			}
		});
		return enterButton;
	}

	private Button createExitButton() {
		Button exitButton = new Button("Exit");
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				menuList.exit();
			}
		});
		return exitButton;
	}

	private Button createRecordButton() {
		Button recorderButton = new Button("Record");
		recorderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mainWindow.handleRecorderButtonPressed();
			}
		});
		return recorderButton;
	}

	public Scene getScene() {
		return scene;
	}

	public MenuList getMenuList() {
		return menuList;
	}

	public StatusBar getStatusBar() {
		return statusBar;
	}

}

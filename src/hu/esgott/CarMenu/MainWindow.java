package hu.esgott.CarMenu;

import hu.esgott.CarMenu.leap.LeapListener;
import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;
import hu.esgott.CarMenu.sound.RecognizerServerConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import com.leapmotion.leap.Controller;

public class MainWindow extends Application {

	private MenuList menuList = new MenuList();
	private StatusBar statusBar = new StatusBar();
	private Controller leapController = new Controller();
	private LeapListener leapListener = new LeapListener(menuList, statusBar);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initializeLeap();
		primaryStage.setTitle("CarMenu");
		Scene scene = new Scene(createPane());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void initializeLeap() {
		leapController.addListener(leapListener);
	}

	private Pane createPane() {
		BorderPane pane = new BorderPane();
		pane.setTop(statusBar.getPanel());
		pane.setCenter(menuList.getList());
		pane.setBottom(createButtons());
		return pane;
	}

	private Pane createButtons() {
		HBox buttonPane = new HBox();
		Button backButton = createBackButton();
		Button forwardButton = createForwardButton();
		Button enterButton = createEnterButton();
		Button exitButton = createExitButton();
		Button connectButton = new Button("Connect");
		connectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				RecognizerServerConnection connection = new RecognizerServerConnection();
				connection.trial();
				connection.dispose();
			}
		});
		buttonPane.getChildren().addAll(backButton, forwardButton, enterButton,
				exitButton, connectButton);
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

	@Override
	public void stop() throws Exception {
		leapListener.dispose();
		leapController.removeListener(leapListener);
	}

}

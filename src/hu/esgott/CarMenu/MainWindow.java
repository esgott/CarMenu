package hu.esgott.CarMenu;

import hu.esgott.CarMenu.leap.LeapListener;
import hu.esgott.CarMenu.menu.MenuList;
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
import com.leapmotion.leap.Listener;

public class MainWindow extends Application {

	private MenuList menuList = new MenuList();
	private Controller leapController = new Controller();
	private Listener leapListener = new LeapListener(menuList);

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
		buttonPane.getChildren().addAll(backButton, forwardButton, enterButton,
				exitButton);
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
		leapController.removeListener(leapListener);
	}

}

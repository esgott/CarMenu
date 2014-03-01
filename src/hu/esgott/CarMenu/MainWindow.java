package hu.esgott.CarMenu;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainWindow extends Application {

	private SelectionModel<String> selectionModel;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("CarMenu");
		Scene scene = new Scene(createPane());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private Pane createPane() {
		BorderPane pane = new BorderPane();
		ListView<String> list = createList();
		pane.setCenter(list);
		Pane buttonPane = createButtons();
		pane.setBottom(buttonPane);
		return pane;
	}

	private ListView<String> createList() {
		ListView<String> list = new ListView<>();
		ObservableList<String> items = FXCollections.observableArrayList("egy",
				"ketto");
		list.setItems(items);
		selectionModel = list.getSelectionModel();
		selectionModel.selectFirst();
		return list;
	}

	private Pane createButtons() {
		HBox buttonPane = new HBox();
		Button backButton = createBackButton();
		Button forwardButton = createForwardButton();
		Button enterButton = new Button("Enter");
		Button exitButton = new Button("Exit");
		buttonPane.getChildren().addAll(backButton, forwardButton, enterButton,
				exitButton);
		return buttonPane;
	}

	private Button createBackButton() {
		Button backButton = new Button("<-");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				selectionModel.selectPrevious();
			}
		});
		return backButton;
	}

	private Button createForwardButton() {
		Button forwardButton = new Button("->");
		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				selectionModel.selectNext();
			}
		});
		return forwardButton;
	}

}

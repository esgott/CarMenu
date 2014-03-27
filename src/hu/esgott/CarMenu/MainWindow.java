package hu.esgott.CarMenu;

import hu.esgott.CarMenu.leap.LeapListener;
import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;
import hu.esgott.CarMenu.sound.RecognizerServerConnection;
import hu.esgott.CarMenu.sound.Recorder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.leapmotion.leap.Controller;

public class MainWindow extends Application {

	private MainWindowScene windowScene = new MainWindowScene(this);
	private Controller leapController = new Controller();
	private LeapListener leapListener = new LeapListener(this);
	private RecognizerServerConnection recognizerConnection;
	private Recorder recorder;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initializeLeap();
		primaryStage.setTitle("CarMenu");
		Scene scene = windowScene.getScene();
		primaryStage.setScene(scene);
		primaryStage.show();
		createRecognizerClasses();
	}

	private void initializeLeap() {
		leapController.addListener(leapListener);
	}

	private void createRecognizerClasses() {
		recognizerConnection = new RecognizerServerConnection(
				windowScene.getMenuList());
		recorder = new Recorder(windowScene.getStatusBar(),
				recognizerConnection);
	}

	public StatusBar getStatusBar() {
		return windowScene.getStatusBar();
	}

	public MenuList getMenuList() {
		return windowScene.getMenuList();
	}

	public Recorder getRecorder() {
		return recorder;
	}

	public void handleRecorderButtonPressed() {
		if (recorder.running()) {
			recorder.stop();
		} else {
			recorder.record();
		}
	}

	@Override
	public void stop() throws Exception {
		leapListener.dispose();
		recorder.dispose();
		recognizerConnection.dispose();
		leapController.removeListener(leapListener);
	}

}

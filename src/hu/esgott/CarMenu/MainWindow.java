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

	private MainWindowScene windowScene;
	private Controller leapController = new Controller();
	private LeapListener leapListener;
	private RecognizerServerConnection recognizerConnection;
	private Recorder recorder;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		windowScene = new MainWindowScene(this);
		primaryStage.setTitle("CarMenu");
		Scene scene = windowScene.getScene();
		primaryStage.setScene(scene);
		primaryStage.show();
		createRecognizerClasses();
		initializeLeap();
	}

	private void initializeLeap() {
		leapListener = new LeapListener(this);
		leapController.addListener(leapListener);
	}

	private void createRecognizerClasses() {
		recognizerConnection = new RecognizerServerConnection();
		recorder = new Recorder(windowScene.getStatusBar(),
				windowScene.getMenuList(), recognizerConnection);
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
		recognizerConnection.dispose();
		leapController.removeListener(leapListener);
	}

}

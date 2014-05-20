package hu.esgott.CarMenu.sound;

import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Recorder {

	private StatusBar statusBar;
	private MenuList menu;
	private RecognizerServerConnection recognizerConnection;
	private RecorderThread recorderThread;
	private Thread thread;

	public Recorder(StatusBar statusBar, MenuList menuList,
			RecognizerServerConnection recognizerConnection) {
		this.statusBar = statusBar;
		menu = menuList;
		this.recognizerConnection = recognizerConnection;
	}

	public void record() {
		if (!running()) {
			statusBar.setRecordMode(true);
			recorderThread = new RecorderThread(recognizerConnection, this);
			thread = new Thread(recorderThread);
			thread.start();
		}
	}

	public synchronized void stop() {
		stopRunningRecording();
		statusBar.setRecordMode(false);
	}

	private void stopRunningRecording() {
		if (recorderThread != null) {
			recorderThread.stop();
			try {
				System.out.println("waiting for thread");
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			recorderThread = null;
		}
	}

	public void matchFound(String response) {
		stop();
		String matchedString = parseResponse(response);
		menu.actionOnRecognizedString(matchedString);
	}

	private String parseResponse(String response) {
		String[] lines = response.split("\n");
		String matchLine = lines[1];
		// third column
		Pattern pattern = Pattern.compile("\\s*\\d+\\s+\\d+\\s+(\\S+)\\s+#.*");
		Matcher matcher = pattern.matcher(matchLine);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			System.out.println("response not understood");
			return "";
		}
	}

	public boolean running() {
		return recorderThread != null;
	}

}

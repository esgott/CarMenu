package hu.esgott.CarMenu.sound;

import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Recorder {

	private StatusBar statusBar;
	private MenuList menu;
	private RecognizerServerConnection recognizerConnection;
	private TargetDataLine inputLine;
	private RecorderThread recorderThread;
	private Thread thread;
	private final static float SAMPLE_RATE = 8000;

	public Recorder(StatusBar statusBar, MenuList menuList,
			RecognizerServerConnection recognizerConnection) {
		this.statusBar = statusBar;
		menu = menuList;
		this.recognizerConnection = recognizerConnection;
		AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
		DataLine.Info inputInfo = new DataLine.Info(TargetDataLine.class,
				format);
		if (!AudioSystem.isLineSupported(inputInfo)) {
			System.out.println("InputLine not supported");
		}
		try {
			inputLine = (TargetDataLine) AudioSystem.getLine(inputInfo);
			inputLine.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void record() {
		if (!running()) {
			statusBar.setRecordMode(true);
			recorderThread = new RecorderThread(inputLine,
					recognizerConnection, this);
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

	public void matchFound(String matchedString) {
		stop();
		menu.actionOnRecognizedString(matchedString);
	}

	public boolean running() {
		return recorderThread != null;
	}

	public void dispose() {
		inputLine.close();
	}

}

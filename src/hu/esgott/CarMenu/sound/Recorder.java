package hu.esgott.CarMenu.sound;

import hu.esgott.CarMenu.menu.StatusBar;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Recorder {

	private StatusBar statusBar;
	private RecognizerServerConnection recognizerConnection;
	private TargetDataLine inputLine;
	private SourceDataLine outputLine;
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	RecorderThread recorderThread;
	Thread thread;

	public Recorder(StatusBar statusBar,
			RecognizerServerConnection recognizerConnection) {
		this.statusBar = statusBar;
		this.recognizerConnection = recognizerConnection;
		AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
		DataLine.Info inputInfo = new DataLine.Info(TargetDataLine.class,
				format);
		DataLine.Info outputInfo = new DataLine.Info(SourceDataLine.class,
				format);
		if (!AudioSystem.isLineSupported(inputInfo)) {
			System.out.println("InputLine not supported");
		}
		if (!AudioSystem.isLineSupported(outputInfo)) {
			System.out.println("OutputLine not supported");
		}
		try {
			inputLine = (TargetDataLine) AudioSystem.getLine(inputInfo);
			inputLine.open(format);
			outputLine = (SourceDataLine) AudioSystem.getLine(outputInfo);
			outputLine.open();
			outputLine.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void record() {
		statusBar.setRecordMode(true);
		outputStream.reset();
		recorderThread = new RecorderThread(inputLine, outputStream,
				recognizerConnection);
		thread = new Thread(recorderThread);
		thread.start();
	}

	public synchronized void stop() {
		if (recorderThread != null) {
			recorderThread.stop();
			try {
				System.out.println("waiting for thread");
				thread.join();
				System.out.println("writing output");
				outputLine.write(outputStream.toByteArray(), 0,
						outputStream.size());
				System.out.println("output written");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			recorderThread = null;
			sendQuery();
		}
		statusBar.setRecordMode(false);
	}

	private void sendQuery() {
		RecognizerCommand command = new RecognizerCommand(ServerCommand.QUERY,
				"", true);
		recognizerConnection.send(command);
	}

	public boolean running() {
		return recorderThread != null;
	}

	public void dispose() {
		inputLine.close();
		outputLine.stop();
		outputLine.drain();
		outputLine.close();
	}

}

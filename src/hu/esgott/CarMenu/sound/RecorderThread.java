package hu.esgott.CarMenu.sound;

import java.awt.Toolkit;
import java.nio.ByteBuffer;

import javax.sound.sampled.TargetDataLine;

public class RecorderThread implements Runnable {

	private static final int BUFFER_SIZE = 1024;
	private static final int QUERY_FREQUENCY = 4;
	private TargetDataLine inputLine;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private RecognizerServerConnection recognizerConnection;
	private Recorder parent;
	private boolean stopped = false;
	private int leftUntilQuery = QUERY_FREQUENCY;

	public RecorderThread(TargetDataLine line,
			RecognizerServerConnection recognizerConnection, Recorder parent) {
		this.inputLine = line;
		this.recognizerConnection = recognizerConnection;
		this.parent = parent;
	}

	@Override
	public void run() {
		System.out.println("recording started");
		beep();
		inputLine.start();
		initServer();

		while (!stopped) {
			inputLine.read(buffer, 0, buffer.length);
			sendRecordedData();
			sendIfQueryExpired();
		}

		inputLine.stop();
		beep();
		System.out.println("recording finished");
	}

	private void beep() {
		Toolkit.getDefaultToolkit().beep();
	}

	private void initServer() {
		RecognizerCommand initCommand = new RecognizerCommand(
				ServerCommand.INIT, "", true);
		recognizerConnection.send(initCommand);
	}

	private void sendRecordedData() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		byteBuffer.put(buffer);
		RecognizerCommand command = new RecognizerCommand(ServerCommand.WAVEIN,
				byteBuffer, false);
		recognizerConnection.send(command);
	}

	private void sendIfQueryExpired() {
		if (leftUntilQuery <= 0) {
			sendQuery();
			leftUntilQuery = QUERY_FREQUENCY;
		}
		leftUntilQuery--;
	}

	private void sendQuery() {
		RecognizerCommand command = new RecognizerCommand(ServerCommand.QUERY,
				"", true);
		command.setCallback(new ResponseCallback() {
			@Override
			public void call(String response) {
				if (response.contains("vit_end=1")) {
					stop();
					recognizerConnection.emptyQueue();
					RecognizerCommand traceBackCommand = new RecognizerCommand(
							ServerCommand.TRACEBACK, "", true);
					traceBackCommand.setCallback(new ResponseCallback() {
						@Override
						public void call(String response) {
							parseServerResponse(response);
						}
					});
					recognizerConnection.send(traceBackCommand);
				}
			}
		});
		recognizerConnection.send(command);
	}

	private void parseServerResponse(String response) {
		char[] splitterChar = new char[1];
		splitterChar[0] = 9;
		String splitterString = new String(splitterChar);
		String[] lines = response.split(splitterString);
		System.out.println("LINES:");
		for (String line : lines) {
			System.out.println(line);
		}
		parent.matchFound(lines[5]);
	}

	public synchronized void stop() {
		stopped = true;
	}

}

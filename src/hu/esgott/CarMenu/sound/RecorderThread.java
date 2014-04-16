package hu.esgott.CarMenu.sound;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.TargetDataLine;

public class RecorderThread implements Runnable {

	private static final int BUFFER_SIZE = 1024;
	private static final int QUERY_FREQUENCY = 4;
	private TargetDataLine line;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private ByteArrayOutputStream outputStream;
	private RecognizerServerConnection recognizerConnection;
	private Recorder parent;
	private boolean stopped = false;
	private int leftUntilQuery = QUERY_FREQUENCY;

	public RecorderThread(TargetDataLine line,
			ByteArrayOutputStream outputStream,
			RecognizerServerConnection recognizerConnection, Recorder parent) {
		this.line = line;
		this.outputStream = outputStream;
		this.recognizerConnection = recognizerConnection;
		this.parent = parent;
	}

	@Override
	public void run() {
		System.out.println("recorder thread started");
		line.start();

		while (!stopped) {
			int numBytesRead = line.read(buffer, 0, buffer.length);
			sendRecordedData();
			sendIfExpired();
			outputStream.write(buffer, 0, numBytesRead);
		}

		line.stop();
		System.out.println("recorder thread finished");
	}

	private void sendRecordedData() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		byteBuffer.put(buffer);
		RecognizerCommand command = new RecognizerCommand(ServerCommand.WAVEIN,
				byteBuffer, false);
		recognizerConnection.send(command);
	}

	private void sendIfExpired() {
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

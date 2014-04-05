package hu.esgott.CarMenu.sound;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.TargetDataLine;

public class RecorderThread implements Runnable {

	private static final int BUFFER_SIZE = 1024;
	private TargetDataLine line;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private ByteArrayOutputStream outputStream;
	private RecognizerServerConnection recognizerConnection;
	private Recorder parent;
	private boolean stopped = false;

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
			sendQuery();
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

	private void sendQuery() {
		RecognizerCommand command = new RecognizerCommand(ServerCommand.QUERY,
				"", true);
		command.setCallback(new ResponseCallback() {
			@Override
			public void call(String response) {
				if (response.contains("vit_end=1")) {
					RecognizerCommand traceBackCommand = new RecognizerCommand(
							ServerCommand.TRACEBACK, "", true);
					recognizerConnection.send(traceBackCommand);
					parent.matchFound();
				}
			}
		});
		recognizerConnection.send(command);
	}

	public void stop() {
		stopped = true;
	}

}

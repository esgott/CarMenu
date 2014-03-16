package hu.esgott.CarMenu.sound;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.TargetDataLine;

public class RecorderThread implements Runnable {

	private TargetDataLine line;
	private byte[] buffer;
	private ByteArrayOutputStream outputStream;
	private boolean stopped = false;

	public RecorderThread(TargetDataLine line, byte[] buffer,
			ByteArrayOutputStream outputStream) {
		this.line = line;
		this.buffer = buffer;
		this.outputStream = outputStream;
	}

	@Override
	public void run() {
		System.out.println("recorder thread started");
		line.start();

		while (!stopped) {
			int numBytesRead = line.read(buffer, 0, buffer.length);
			outputStream.write(buffer, 0, numBytesRead);
		}

		line.stop();
		System.out.println("recorder thread finished");
	}

	public void stop() {
		stopped = true;
	}

}

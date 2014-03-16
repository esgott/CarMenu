package hu.esgott.CarMenu.sound;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Recorder {

	private TargetDataLine inputLine;
	private SourceDataLine outputLine;
	private byte[] buffer;
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	RecorderThread recorderThread;
	Thread thread;

	public Recorder() {
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
			buffer = new byte[inputLine.getBufferSize() / 5];
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void record() {
		recorderThread = new RecorderThread(inputLine, buffer, outputStream);
		thread = new Thread(recorderThread);
		thread.start();
	}

	public void stop() {
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
		}
	}

	public void dispose() {
		inputLine.close();
		outputLine.stop();
		outputLine.drain();
		outputLine.close();
	}

}

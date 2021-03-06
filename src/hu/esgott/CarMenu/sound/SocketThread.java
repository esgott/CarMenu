package hu.esgott.CarMenu.sound;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class SocketThread implements Runnable {

	private BlockingQueue<RecognizerCommand> queue;
	private SocketFactory socketFactory;
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private boolean running = true;

	public SocketThread(SocketFactory socketFactory) {
		this.socketFactory = socketFactory;
		this.queue = socketFactory.createQueue();
	}

	public void sendCommand(RecognizerCommand command) {
		try {
			queue.put(command);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			connect();
			System.out.println("connected");
			while (running) {
				sendNextCommand();
			}
			System.out.println("socket thread stopped");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					System.out.println("closing socket");
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void connect() throws IOException {
		socket = socketFactory.createSocket();
		socket.setSoTimeout(5000);
		inputStream = socketFactory.createInputStream(socket);
		outputStream = socketFactory.createOutoutStream(socket);
	}

	public void sendNextCommand() throws IOException, InterruptedException {
		RecognizerCommand command = queue.poll(500, TimeUnit.MILLISECONDS);
		if (command != null) {
			if (command.binary()) {
				sendTextData(command.getCommand());
				sendBinaryData(command.getBinaryData());
			} else {
				sendTextData(command.getCommandWithParameters());
			}
			if (command.waitForResponse()) {
				receive(command);
			}
		}
	}

	private void sendTextData(String text) throws IOException {
		byte[] data = text.getBytes();
		sendSize(data.length);
		outputStream.write(data);
		outputStream.flush();
		System.out.println(text + " command sent");
	}

	private void sendSize(int length) throws IOException {
		ByteBuffer lengthBytes = packLength(length);
		for (int i = 0; i < 4; i++) {
			outputStream.write(lengthBytes.get(i));
		}
	}

	private ByteBuffer packLength(int length) {
		ByteBuffer lengthBytes = ByteBuffer.allocate(4);
		lengthBytes.order(ByteOrder.LITTLE_ENDIAN);
		lengthBytes.putInt(length);
		return lengthBytes;
	}

	private void sendBinaryData(ByteBuffer buffer) throws IOException {
		sendSize(buffer.limit() / 2);
		for (int i = 0; i < buffer.limit(); i++) {
			outputStream.write(buffer.get(i));
		}
		outputStream.flush();
		System.out.println(buffer.limit() + " binary data sent ");
	}

	private void receive(RecognizerCommand command) throws IOException {
		int length = receiveSize();
		System.out.println("receiving " + length + " bytes of data");
		String responseString = receiveResponse(length);
		System.out.println(responseString + "received");
		command.call(responseString);
	}

	private int receiveSize() throws IOException {
		ByteBuffer size = ByteBuffer.allocate(4);
		size.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < 4; i++) {
			byte nextByte = (byte) inputStream.read();
			if (nextByte >= 0) {
				size.put(nextByte);
			} else {
				System.out.println("read returned " + nextByte);
				stop();
				return 0;
			}
		}
		return size.getInt(0);
	}

	private String receiveResponse(int size) throws IOException {
		byte[] response = new byte[size];
		int read = 0;
		while (running && read == 0) {
			try {
				read = inputStream.read(response);
			} catch (SocketTimeoutException e) {
				System.out.println("receive timed out");
			}
		}
		return new String(response);
	}

	public void emptyQueue() {
		queue.clear();
	}

	public void stop() {
		running = false;
	}

}

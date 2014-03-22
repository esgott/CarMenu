package hu.esgott.CarMenu.sound;

import hu.esgott.CarMenu.menu.MenuList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SocketThread implements Runnable {

	private LinkedBlockingQueue<RecognizerCommand> queue = new LinkedBlockingQueue<>();
	private String ip;
	private int port;
	private Socket socket;
	private BufferedReader inputStream;
	private BufferedWriter outputStream;
	private MenuList menu;
	private boolean running = true;

	public SocketThread(String serverIp, int serverPort, MenuList menuList) {
		ip = serverIp;
		port = serverPort;
		menu = menuList;
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
			System.out.println("thread started, connceting...");
			connect();
			System.out.println("connected");
			while (running) {
				System.out.println("waiting for command");
				sendNextCommand();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void connect() throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		inputStream = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		outputStream = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
	}

	private void sendNextCommand() throws IOException, InterruptedException {
		RecognizerCommand command = queue.poll(100, TimeUnit.MILLISECONDS);
		if (command != null) {
			System.out.println("start sending command");
			ByteBuffer length = command.getCommandLength();
			for (int i = 0; i < 4; i++) {
				outputStream.write(length.get(i));
			}
			String commandString = command.getCommandWithParameters();
			outputStream.write(commandString);
			outputStream.flush();
			System.out.println("command sent");
			receive();
			System.out.println("response received");
		} else {
			System.out.println("no command in timeout");
		}
	}

	private void receive() throws IOException {
		int length = receiveSize();
		char[] response = new char[length];
		inputStream.read(response);
		menu.actionOnRecognizedString("");
		System.out.println(response);
	}

	private int receiveSize() throws IOException {
		ByteBuffer size = ByteBuffer.allocate(4);
		size.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < 4; i++) {
			byte nextByte = (byte) inputStream.read();
			size.put(nextByte);
		}
		return size.getInt(0);
	}

	public void stop() {
		running = false;
	}

}

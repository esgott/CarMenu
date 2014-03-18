package hu.esgott.CarMenu.sound;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RecognizerServerConnection {

	private Socket socket;
	private BufferedReader inputStream;
	private BufferedWriter outputStream;
	private static final String SERVER_IP = "152.66.246.33";
	private static final int SERVER_PORT = 2605;

	public RecognizerServerConnection() {
		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
			inputStream = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			outputStream = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void trial() {
		String command = "CMD_LOAD_GRAMMAR_FILE";
		String parameters = "SRC=something.flx B_ACTIVATE=false";
		try {
			System.out.println("sending command");
			send(command + parameters);
			System.out.println(receive());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String command) throws IOException {
		writeSize(command.length());
		outputStream.write(command);
		outputStream.flush();
	}

	private void writeSize(int size) throws IOException {
		ByteBuffer commandLength = ByteBuffer.allocate(4);
		commandLength.order(ByteOrder.LITTLE_ENDIAN);
		commandLength.putInt(size);
		for (int i = 0; i < 4; i++) {
			outputStream.write(commandLength.get(i));
		}
	}

	public String receive() throws IOException {
		int length = receiveSize();
		char[] response = new char[length];
		inputStream.read(response);
		return new String(response);
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

	public void dispose() {
		try {
			System.out.println("closing socket");
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package hu.esgott.CarMenu.sound;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class RecognizerServerConnection {

	private Socket socket;
	private BufferedReader inputStream;
	private BufferedWriter outputStream;
	private Scanner scanner;
	private static final String SERVER_IP = "152.66.246.33";
	private static final int SERVER_PORT = 2605;

	public RecognizerServerConnection() {
		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
			inputStream = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			outputStream = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			scanner = new Scanner(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void trial() {
		String command = "CMD_LOAD_GRAMMAR_FILE";
		String parameters = "SRC=something.flx B_ACTIVATE=false";
		try {
			outputStream.write(command.length());
			outputStream.write(command);
			outputStream.write(parameters);
			int length = scanner.nextInt();
			char[] response = new char[length];
			inputStream.read(response);
			String responseString = new String(response);
			System.out.println(responseString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dispose() {
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

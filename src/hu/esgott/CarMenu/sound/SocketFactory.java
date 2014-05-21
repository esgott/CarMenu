package hu.esgott.CarMenu.sound;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketFactory {

	private String ip;
	private int port;

	public SocketFactory(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public Socket createSocket() throws IOException {
		return new Socket(ip, port);
	}

	public InputStream createInputStream(Socket socket) throws IOException {
		return new BufferedInputStream(socket.getInputStream());
	}

	public OutputStream createOutoutStream(Socket socket) throws IOException {
		return new BufferedOutputStream(socket.getOutputStream());
	}

	public BlockingQueue<RecognizerCommand> createQueue() {
		return new LinkedBlockingQueue<>();
	}

}

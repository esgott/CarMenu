package hu.esgott.CarMenu.sound;


public class RecognizerServerConnection {

	private SocketThread socketThread;
	private Thread thread;

	public RecognizerServerConnection() {
		socketThread = new SocketThread("152.66.246.33", 2605);
		thread = new Thread(socketThread);
		System.out.println("starting thread");
		thread.start();
	}

	public void trial() {
		RecognizerCommand command = new RecognizerCommand(ServerCommand.LOAD_GRAMMAR, "SRC=something.flx B_ACTIVATE=false");
		socketThread.sendCommand(command);
	}

	public void dispose() {
		socketThread.stop();
		try {
			thread.join(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

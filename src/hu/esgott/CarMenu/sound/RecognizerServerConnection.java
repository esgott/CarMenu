package hu.esgott.CarMenu.sound;

public class RecognizerServerConnection {

	private final static String GRAMMAR_FILE = "lex_sp_00149.flx";
	private SocketThread socketThread;
	private Thread thread;

	public RecognizerServerConnection() {
		socketThread = new SocketThread("152.66.246.33", 2605);
		thread = new Thread(socketThread);
		System.out.println("starting thread");
		thread.start();
		connect();
	}

	private void connect() {
		String loadParameters = "SRC=" + GRAMMAR_FILE + " B_ACTIVATE=false";
		String deactivateParameters = "ID=ID_ALL_GRAMMARS";
		String activateParameters = "ID=" + GRAMMAR_FILE;
		socketThread.sendCommand(new RecognizerCommand(
				ServerCommand.LOAD_GRAMMAR, loadParameters, true));
		socketThread.sendCommand(new RecognizerCommand(
				ServerCommand.DEACTIVATE_GRAMMAR, deactivateParameters, true));
		socketThread.sendCommand(new RecognizerCommand(
				ServerCommand.ACTIVATE_GRAMMAR, activateParameters, true));
		socketThread.sendCommand(new RecognizerCommand(ServerCommand.INIT, "",
				true));
	}

	public void send(RecognizerCommand command) {
		socketThread.sendCommand(command);
	}

	public void emptyQueue() {
		socketThread.emptyQueue();
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

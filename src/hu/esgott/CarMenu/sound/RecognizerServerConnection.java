package hu.esgott.CarMenu.sound;

import hu.esgott.CarMenu.menu.MenuList;

public class RecognizerServerConnection {

	private SocketThread socketThread;
	private Thread thread;

	public RecognizerServerConnection(MenuList menu) {
		socketThread = new SocketThread("152.66.246.33", 2605, menu);
		thread = new Thread(socketThread);
		System.out.println("starting thread");
		thread.start();
		connect();
	}

	private void connect() {
		socketThread.sendCommand(new RecognizerCommand(
				ServerCommand.LOAD_GRAMMAR,
				"SRC=lex_sp_00139.flx B_ACTIVATE=false", true));
		socketThread.sendCommand(new RecognizerCommand(
				ServerCommand.DEACTIVATE_GRAMMAR, "ID=ID_ALL_GRAMMARS", true));
		socketThread.sendCommand(new RecognizerCommand(
				ServerCommand.ACTIVATE_GRAMMAR, "ID=lex_sp_00139.flx", true));
		socketThread.sendCommand(new RecognizerCommand(ServerCommand.INIT, "",
				true));
	}

	public void send(RecognizerCommand command) {
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

package hu.esgott.CarMenu.sound;

public enum ServerCommand {
	LOAD_GRAMMAR("CMD_LOAD_GRAMMAR_FILE");

	private final String command;

	ServerCommand(String command) {
		this.command = command;
	}

	String getCommand() {
		return command;
	}
}

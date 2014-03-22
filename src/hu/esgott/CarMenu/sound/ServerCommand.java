package hu.esgott.CarMenu.sound;

public enum ServerCommand {
	LOAD_GRAMMAR("CMD_LOAD_GRAMMAR_FILE"), DEACTIVATE_GRAMMAR(
			"CMD_DEACTIVATE_GRAMMAR"), ACTIVATE_GRAMMAR("CMD_ACTIVATE_GRAMMAR"), INIT(
			"CMD_INIT"), WAVEIN("CMD_WAVEIN"), QUERY("CMD_QUERYVIT"), TRACEBACK(
			"CMD_TRACEBACK");

	private final String command;

	ServerCommand(String command) {
		this.command = command;
	}

	String getCommand() {
		return command;
	}
}

package hu.esgott.CarMenu.sound;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RecognizerCommand {

	private String command;
	private String parameters;
	private ByteBuffer binaryData;

	public RecognizerCommand(ServerCommand serverCommand, String parameters) {
		command = serverCommand.getCommand();
		this.parameters = " " + parameters;
	}

	public RecognizerCommand(ServerCommand serverCommand, ByteBuffer binaryData) {
		this(serverCommand, "");
		binaryData.order(ByteOrder.LITTLE_ENDIAN);
		this.binaryData = binaryData;
	}

	public ByteBuffer getCommandLength() {
		int length;
		if (binaryData == null) {
			length = command.length() + parameters.length();
		} else {
			length = 0; // TODO calculate length in this case
		}
		ByteBuffer lengthBytes = ByteBuffer.allocate(4);
		lengthBytes.order(ByteOrder.LITTLE_ENDIAN);
		lengthBytes.putInt(length);
		return lengthBytes;
	}

	public String getCommandWithParameters() {
		return command + parameters;
	}

}

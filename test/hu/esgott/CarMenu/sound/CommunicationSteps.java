package hu.esgott.CarMenu.sound;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class CommunicationSteps {
	private SocketThread socketThread;
	private InputStream mockInputStream;
	private OutputStream mockOutputStream;
	private byte[] commandData;

	@Given("that connection to the server was successful")
	public void createConnection() throws IOException {
		SocketFactory mockSocketFactroy = createMockBuilder(SocketFactory.class)
				.addMockedMethods("createSocket", "createInputStream",
						"createOutoutStream").createMock();
		socketThread = new SocketThread(mockSocketFactroy);
		insertMockStreams(mockSocketFactroy);
	}

	private void insertMockStreams(SocketFactory mockSocketFactroy)
			throws IOException {
		Socket mockSocket = createStrictMock(Socket.class);
		mockInputStream = createStrictMock(InputStream.class);
		mockOutputStream = createStrictMock(OutputStream.class);
		expect(mockSocketFactroy.createSocket()).andReturn(mockSocket);
		expect(mockSocketFactroy.createInputStream(mockSocket)).andReturn(
				mockInputStream);
		expect(mockSocketFactroy.createOutoutStream(mockSocket)).andReturn(
				mockOutputStream);
		replay(mockSocketFactroy);
		socketThread.connect();
	}

	@When("the command sent is $command")
	public void putCommandInQueue(String command) {
		commandData = command.getBytes();
		String[] words = command.split(" ");
		ServerCommand serverCommand = ServerCommand.findByCommandText(words[0]);
		String parameters = command.replaceFirst(words[0] + " ", "");
		RecognizerCommand recognizerCommand = new RecognizerCommand(
				serverCommand, parameters, false);
		socketThread.sendCommand(recognizerCommand);
	}

	@When("the binary command sent is $command")
	public void putBinaryCommandInQueue(List<Integer> data) {
		commandData = new byte[data.size()];
		for (int i = 0; i < commandData.length; i++) {
			int current = data.get(i);
			commandData[i] = (byte) current;
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(commandData.length);
		byteBuffer.put(commandData);
		RecognizerCommand recognizerCommand = new RecognizerCommand(
				ServerCommand.WAVEIN, byteBuffer, false);
		socketThread.sendCommand(recognizerCommand);
	}

	@Then("the deciaml size bytes sent in order are $sizeBytes and the command sent out")
	public void checkSending(List<Integer> sizeBytes) throws IOException,
			InterruptedException {
		for (int sizeByte : sizeBytes) {
			mockOutputStream.write(sizeByte);
		}
		mockOutputStream.write(aryEq(commandData));
		mockOutputStream.flush();
		replay(mockOutputStream);
		socketThread.sendNextCommand();
		verify(mockOutputStream);
	}

	@Then("the CMD_WAVEIN with size, the decimal size biytes $sizeBytes and the binary data sent out")
	public void checkSendingOfWaveIn(List<Integer> sizeBytes)
			throws IOException, InterruptedException {
		mockOutputStream.write(10);
		mockOutputStream.write(0);
		mockOutputStream.write(0);
		mockOutputStream.write(0);
		mockOutputStream.write(aryEq(ServerCommand.WAVEIN.getCommand()
				.getBytes()));
		mockOutputStream.flush();
		for (int sizeByte : sizeBytes) {
			mockOutputStream.write(sizeByte);
		}
		for (byte actual : commandData) {
			mockOutputStream.write(actual);
		}
		mockOutputStream.flush();
		replay(mockOutputStream);
		socketThread.sendNextCommand();
		verify(mockOutputStream);
	}
}

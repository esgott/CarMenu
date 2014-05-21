package hu.esgott.CarMenu.sound;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import hu.esgott.CarMenu.menu.MenuList;
import hu.esgott.CarMenu.menu.StatusBar;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class ParseSteps {
	private Recorder recorder;
	private String response;
	private MenuList mockMenuList;

	@Given("that the response arrives")
	public void setUpRecorder() {
		mockMenuList = createMock(MenuList.class);
		StatusBar mockStatusBar = createMock(StatusBar.class);
		RecognizerServerConnection mockRecognizerServerConncection = createMock(RecognizerServerConnection.class);
		recorder = new Recorder(mockStatusBar, mockMenuList,
				mockRecognizerServerConncection);
	}

	@When("the response is $response")
	public void storeResponse(String response) {
		this.response = response;
	}

	@Then("the match decoded is $match")
	public void decodedMatchShouldBe(String match) {
		mockMenuList.actionOnRecognizedString(match);
		replay(mockMenuList);
		recorder.matchFound(response);
		verify(mockMenuList);
	}
}

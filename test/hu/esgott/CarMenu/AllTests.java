package hu.esgott.CarMenu;

import hu.esgott.CarMenu.sound.ResponseParsing;
import hu.esgott.CarMenu.sound.ServerCommunication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ResponseParsing.class, ServerCommunication.class })
public class AllTests {

}

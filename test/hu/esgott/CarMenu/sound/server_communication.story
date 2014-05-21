Server Communication

Narrative:
In order to communicate with the recognizer server
As a developer
I want to ensure the program sends and receives the messages correctly

Scenario: Textual data sent to recognizer server
Given that connection to the server was successful
When the command sent is
CMD_LOAD_GRAMMAR_FILE SRC=lex_sp_00001.flx B_ACTIVATE=false
Then the deciaml size bytes sent in order are 59,0,0,0 and the command sent out

Scenario: Textual data sent to recognizer server and the size sent in multiple bytes
Given that connection to the server was successful
When the command sent is
CMD_LOAD_GRAMMAR_FILE SRC=lex_sp_00001.flx B_ACTIVATE=false DUMMY_PARAMETER=dummy_value DUMMY_PARAMETER2=dummy_value2 DUMMY_PARAMETER3=dummy_value3 DUMMY_PARAMETER4=dummy_value4 DUMMY_PARAMETER5=dummy_value5 DUMMY_PARAMETER6=dummy_value6 DUMMY_PARAMETER7=dummy_value7
Then the deciaml size bytes sent in order are 11,1,0,0 and the command sent out

Scenario: Binary data sent to recognizer server, with WAVE_IN command and the binary size is halfed
Given that connection to the server was successful
When the binary command sent is
1,2,3,4,5,6,7,8,9,0,1,2
Then the CMD_WAVEIN with size, the decimal size biytes 6,0,0,0 and the binary data sent out

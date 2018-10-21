package test;

import java.util.List;

public class TestCase {
	String regex;
	int numStatesNFA;
	
	public TestCase(String regex) {
		this.regex = regex;
	}
	
	public TestCase(String regex, int numStates) {
		this.regex = regex;
		numStatesNFA = numStates;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public int getNumStatesNFA() {
		return numStatesNFA;
	}

	public void setNumStatesNFA(int numStatesNFA) {
		this.numStatesNFA = numStatesNFA;
	}
	
	
	

}

package accepter;

import java.util.ArrayList;

import state.State;
import state.StateList;

public class StateAccepter {
	public static boolean evaluate(StateList automata, String input) {
		System.out.println("Reading " + input);
		
		State currState = automata.get(automata.getStartingStateIndices().get(0));
		while(input.length() != 0) {
			String transitionInput = String.valueOf(input.charAt(0));
			System.out.println("GOING FOR " + transitionInput);
			
			input = input.substring(1);

			currState = getNextTransition(currState, transitionInput);
			
			if(currState == null) {
				return false;
			}
		}
		
		if(currState.isAccepting){
			return true;
		}else {
			return false;
		}
	}
	
	public static State getNextTransition(State currState, String transitionInput) {
		return currState.getTransition(transitionInput).size() == 0 ? 
				null : 
				currState.getTransition(transitionInput).get(0);
	}
	
	public static int getIndexOfLongestAcceptableString(StateList automata, String input) {
		State currState = automata.get(automata.getStartingStateIndices().get(0));
		
		int longestPossible = -1;
		for(int j = 0; j < input.length(); j++) {
			currState = getNextTransition(currState, String.valueOf(input.charAt(j)));
			if(currState == null) {
//				System.out.println("HALT POSSIBLE: " + longestPossible);
				return longestPossible;
			}else if(currState.isAccepting == true) {
//				System.out.println("FOUND");
				longestPossible = j;
			}
		}
//		System.out.println("LONGEST POSSIBLE: " + longestPossible);
		return longestPossible;
	}
//	(a|b)*
	
	public static void getAllAcceptingSubStrings(StateList automata, String textFile, String regex) {
		System.out.println("Location of strings accepted by regex: " + regex);
		System.out.println();
		for(int i = 0; i < textFile.length(); i++) {
//			System.out.println("STARTING AT INDEX " + i);
			int startingIndex = i;
			int endingIndex = i + getIndexOfLongestAcceptableString(automata, textFile.substring(i));
//			System.out.println("Starting: " + startingIndex);
//			System.out.println("Ending  : " + endingIndex);
			if(startingIndex <= endingIndex) {
//				System.out.println(startingIndex + " to " + endingIndex + "(" + textFile.substring(startingIndex, endingIndex+1) + ")");
				System.out.println(startingIndex + " to " + endingIndex);
				System.out.println(textFile.substring(0, startingIndex) + "[" + textFile.substring(startingIndex, endingIndex+1) + "]" + textFile.substring(endingIndex+1));
				System.out.println();
			}
		}
		
	}
}

package accepter;

import java.util.ArrayList;

import state.State;
import state.StateConstructor;
import state.StateList;

// 1 - HP
// Potter

// 2 - ab
// (ab)+

// 3 - float
// [0-9]*.[0-9]+

// 4 - date
// ([0-9][0-9]/([0-9][0-9]|[A-Z][a-z][a-z])/[0-9][0-9][0-9][0-9])|([0-9][0-9][0-9][0-9]/[0-9][0-9]/[0-9][0-9])|([0-9][0-9] [A-Z][a-z][a-z] [0-9][0-9][0-9][0-9])
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
		int longestPossible = -1;
		if(automata.getStartingStateIndices().size() > 0){
			State currState = automata.get(automata.getStartingStateIndices().get(0));
			
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
		}
		return longestPossible;
	}

	public static int[][] getAllAcceptingSubStrings(StateList automata, String textFile) {

		int[][] acceptingIndices = new int[textFile.length()][2];
		int count = 0;
		
		if(textFile.length() == 0) {
			if(automata.size() ==  1) {
				State currState = getNextTransition(automata.get(0), "");
				if(currState == null) {
					currState = getNextTransition(automata.get(0), StateConstructor.STR_EPSILON);
					if(currState != null) {
						if(currState.isAccepting) {
							acceptingIndices = new int[1][2];
							acceptingIndices[0][0] = 0;
							acceptingIndices[0][1] = 0;
							count++;	
						}	
					}
				}else {
					if(currState.isAccepting) {
						acceptingIndices = new int[1][2];
						acceptingIndices[0][0] = 0;
						acceptingIndices[0][1] = 0;
						count++;	
					}
				}
			}
		}else {
			for(int i = 0; i < textFile.length(); i++) {
				int startingIndex = i;
				int endingIndex = i + getIndexOfLongestAcceptableString(automata, textFile.substring(i));
				acceptingIndices[i][0] = startingIndex;
				acceptingIndices[i][1] = endingIndex;
				if(startingIndex <= endingIndex) {
					count++;
				}
			}	
		}
		
		int ctr = 0;
		
		int[][] finalAcceptingIndices = new int[count][2];
		for(int i = 0; i < acceptingIndices.length; i++) {
			if(acceptingIndices[i][0] <= acceptingIndices[i][1]) {
				finalAcceptingIndices[ctr][0] = acceptingIndices[i][0];
				finalAcceptingIndices[ctr][1] = acceptingIndices[i][1];
				ctr++;
			}
		}
		
		return finalAcceptingIndices;
	}
}

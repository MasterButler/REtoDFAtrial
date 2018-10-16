package state;

import java.util.LinkedList;
import java.util.Map;

public class StateConstructor {
	
	// a OR b
	public static char OR_OPERATOR = '|';
	// a >= 0
	public static char POSITIVE_CLOSURE = '+';
	// ONE a OR NO a
	public static char QUESTION_MARK = '?';
	// FREE PASS
	public static char EPSILON = '~';
	
	public static String STR_EPSILON = "~";
	
	public static void construct(String regexInput) {
		StateList stateList = new StateList();
		String regex = regexInput;
		
		int state = 0;
		
		State epsilonStart = null;
		State epsilonEnd = null;
	
		State from = null;
		State to = null;
		String transitionInput = null;
		
		boolean hasOR = false;

		
		try {
			while(!regex.isEmpty()) {
//				System.out.println("STARTING");
				
				// the OR case (e.g. a | b)
				if(regex.charAt(0) == OR_OPERATOR) {
					// retain the epsilonStart and epsilonEnd of previous state
					System.out.println("FOUND AN OR OPERATOR");
					hasOR = true;
					regex = regex.substring(1);
				}
				// the POSITIVE CLOSURE case (e.g. a"+" )
				else if(regex.charAt(0) == POSITIVE_CLOSURE) {
					to.setTransition(transitionInput, from);
					regex = regex.substring(1);
				}
				// the POSITIVE CLOSURE case (e.g. a"?")
				else if(regex.charAt(0) == QUESTION_MARK) {
					from.setTransition(STR_EPSILON, to);
					regex = regex.substring(1);
				}
				// the simple character case (alphanumerics)
				else {
//					System.out.println("FIRST ALPHA NUMERIC");
					
					if(regex.charAt(0) == '(') {
						LinkedList<Character> inputStack = new LinkedList<Character>();
						
						System.out.println("TRYING here");
						String toCheck = regex;
						
						transitionInput = toCheck.charAt(0) + "";
						
						inputStack.add(toCheck.charAt(0));
						toCheck = toCheck.substring(1);
						
						while(inputStack.size() > 0) {
							if(toCheck.charAt(0) == '(') {
								inputStack.add(toCheck.charAt(0));
							}else if(toCheck.charAt(0) == ')') {
								inputStack.removeLast();
							}
							transitionInput += toCheck.charAt(0);
							toCheck = toCheck.substring(1);
						}
						System.out.println("REGEX: " + regex);
						System.out.println("TRANS: " + transitionInput);
						System.out.println("LEN OF TRANSITION OUTPUT:");
						regex = regex.substring(transitionInput.length());
					}else {
						transitionInput = regex.charAt(0) + "";
						regex = regex.substring(1);
					}
					
					if(!hasOR) {
						if(epsilonStart != null && epsilonStart != null) {
							epsilonEnd.setTransition(STR_EPSILON, epsilonStart);
						}
						// System.out.println("CREATING EPSILON STATES");
						epsilonStart = new State("s" + state);
					}
					
					if(transitionInput.length() == 1) {						
						from = new State("q" + state + "0");
						to = new State("q" + state + "1");
						from.setTransition(transitionInput, to);
					}else {
						/* TODO: THIS IS WRONG. UPDATE THIS SHIT
						 */
						from = new State("q" + state + "0");
						to = new State("q" + state + "1");
						from.setTransition(transitionInput, to);
					}
					
					epsilonStart.setTransition(STR_EPSILON, from);
					
					if(!hasOR) {
						epsilonEnd = new State("e" + state);
					}
					
					to.setTransition(STR_EPSILON, epsilonEnd);
					state++;
					
					stateList.add(epsilonStart);
					stateList.add(epsilonEnd);
					stateList.add(from);
					stateList.add(to);
					hasOR = false;
				}
				
				
				
			}	
		}catch(Exception e) {
			System.out.println("ERROR OCCURED AT CLUSTER #1");
			e.printStackTrace();
		}
		
		
		// BREAK DOWN CASES WHERE THERE ARE CLUSTERS FORMED IN TRANSITION INPUTS
		try {
			for(int i = 0; i < stateList.size(); i++) {
				System.out.println("heh");
			}
		}catch(Exception e) {
			System.out.println("ERROR OCCURED AT CLUSTER #2");
			e.printStackTrace();
		}
		
		// MINIMIZE THE STATES
		
		// PRINT OUT THE STATE
		System.out.println("TOTAL NUMBER OF STATES: " + stateList.size() + "\n");
		for(int i = 0; i < stateList.size(); i++) {
			String currState = stateList.get(i).name;
			
			System.out.println(currState);
			Map<String, StateList> currMap = stateList.get(i).connectedStates;
			for(String key: currMap.keySet()) {
	
				StateList transitionOutput = currMap.get(key);
				if(transitionOutput.size() == 0) {
					System.out.println("DEAD END");
				}else {
					for(int j= 0; j < transitionOutput.size(); j++) {					
						System.out.println("(" + currState + ", " + key + ") = " + transitionOutput.get(j).name);
					}					
				}
					
				
			}
			
			System.out.println();
			
		}
	}
}

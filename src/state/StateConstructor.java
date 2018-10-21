package state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

//((ab)|cd)ef|ghi
//( (ab) | cd ) ef | ghi

public class StateConstructor {
	
	// a OR b
	public static char OR_OPERATOR = '|';
	// a >= 1
	public static char POSITIVE_CLOSURE = '+';
	// a >= 0
	public static char ASTERISK_CLOSURE = '*';
	// MAY or MAY NOT exist
	public static char QUESTION_MARK = '?';
	// FREE PASS
	public static char EPSILON = '~';
	
	public static String STR_EPSILON = "~";
	
	public static StateList REtoDFA(String regexInput) {
		StateList stateList = new StateList();
		
		if(regexInput.trim().equals("")) {
			State forBlank = new State("s0");
			forBlank.isAccepting = true;
			forBlank.setTransition(STR_EPSILON, forBlank);
			
			stateList.add(forBlank);
			printStateTransitions(stateList);
		}else {
			regexInput = addParenthesisPrecedences(regexInput);
			System.out.println("To solve: " + regexInput);
			stateList = REtoENFA(regexInput);
			stateList = ENFAtoDFA(stateList);			
			for(int i = 0; i < stateList.size(); i++) {
				stateList.get(i).name = "q" + i;
			}
			printStateTransitions(stateList);
		}
		
		return stateList;
	}
	
	public static StateList ENFAtoDFA(StateList stateList) {		
		// Generate E-Closures of Each for easy indexing later on.
		System.out.println("EPSILON CLOSURES");
		StateList groupedList = new StateList();
		for(int i = 0; i < stateList.size(); i++) {
			StateList eClosure = stateList.get(i).generateEClosures();
//			System.out.print("E(" + stateList.get(i).name + ")\t= ");
			for(int j = 0; j < eClosure.size(); j++) {
				System.out.print(eClosure.get(j).name + " ");
			}
			System.out.println();
			StateGroup toDO = new StateGroup(eClosure);
			groupedList.add(toDO);
		}
		
		StateList finalList = new StateList();
		
		StateGroup toConnect = null;
		StateList toTraverse = new StateList();
		
		StateGroup initialState = ((StateGroup) groupedList.get(0));
		toTraverse.add( initialState );
		finalList.add( initialState );
		while(toTraverse.size() > 0) {
			toConnect = (StateGroup) toTraverse.remove(0);			
			if(!toConnect.hasBeenTraversed) {
//				finalList.add(toConnect);
				ArrayList<String> keySets = toConnect.usedStates.getAllKeySets();
				for(int i = 0; i < keySets.size(); i++) {
					if(!keySets.get(i).equals(STR_EPSILON)) {						
						String connectedStateName = toConnect.usedStates.predictConnection(keySets.get(i));
//						System.out.println("SEARCHING FOR UNIT WITH NAME " + connectedStateName);
						int connectedStateIndex = groupedList.getIndexIfExisting(connectedStateName);
						StateGroup connectedState = null;
						if(connectedStateIndex == -1) {
//							groupedList.createNewState();
//							System.out.println("heh");
							StateGroup newConnection = toConnect.usedStates.createNewConnection(keySets.get(i));
							groupedList.add(newConnection);
							connectedState = newConnection;
						}else {
							connectedState = (StateGroup)groupedList.get(connectedStateIndex);							
						}
						
						toTraverse.add(connectedState);
						finalList.add(connectedState);
						toConnect.setTransition(keySets.get(i), connectedState);
//						System.out.println("(" + groupedList.get(i).name + ", " + keySets.get(i) + ") = " + "E(" + connectedStateName + ")");
						// 
					}
				}
			}
			toConnect.hasBeenTraversed = true;
		}
		
		System.out.println("============================");
		System.out.println("DFA STATE TRANSITION");
		System.out.println("============================");
		printStateTransitions(finalList);
		
		return finalList;
	}
	
	public static StateList REtoENFA(String regexInput) {
		System.out.println();
		StateList stateList = construct(regexInput, "");
		
		stateList.get(stateList.getStartingStateIndex()).setStarting(true);
		stateList.get(stateList.getEndingStateIndex()).setAccepting(true);
		
		// BREAK DOWN CASES WHERE THERE ARE CLUSTERS FORMED IN TRANSITION INPUTS
		try {
			boolean isAllChecked = false;
			int i = 0;
			while(!isAllChecked) {
//				System.out.println("==" + i + "==");
				if(i == stateList.size()-1) {
					isAllChecked = true;
				}
				String currState = stateList.get(i).name;
				
//				System.out.println(currState);
				Map<String, StateList> currMap = stateList.get(i).connectedStates;
//				System.out.println("KEYS AVAILABLE: ");
				for(String key: currMap.keySet()) {
//					System.out.println(key);
					if(key.length() > 1) {
						String rootStateNumber = currState.substring(1) + ".";
						
//						System.out.println("EDITING " + currState + "WITH INPUT " + key);
//						System.out.println("EDITED STATE NO: " + rootStateNumber);
//						System.out.println("EDITED KEY     : " + key.substring(1, key.length()-1));
						
						StateList newStates = construct( key.substring(1, key.length()-1), rootStateNumber ); 
						
//						System.out.println("REROUTING");
//						printStateTransitions(newStates);						
						stateList.addAll(newStates);
//						System.out.println("END OF NEW STATE");

						
						// connect to start of new set
						stateList.get(i).setTransition(STR_EPSILON, newStates.get(newStates.getStartingStateIndex()));
						// connect to end of new set
						newStates.get(newStates.getEndingStateIndex()).setTransition(STR_EPSILON, stateList.get(i).getTransition(key));
						
						// sever connection of previously connected complicated shit
						stateList.get(i).removeTransitionWithInputOf(key);
						
						// restart the check
						i = 0;
						isAllChecked = false;
						break;
					}
				}
				
				i++;
				
			}


			System.out.println("============================");
			System.out.println("EPSILON NFA STATE TRANSITION");
			System.out.println("============================");
			printStateTransitions(stateList);
		}catch(Exception e) {
			System.out.println("ERROR OCCURED AT CLUSTER #2");
			e.printStackTrace();
		}
		return stateList;
	}
	
	public static StateList construct(String regexInput, String rootState) {
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
//					System.out.println("FOUND AN OR OPERATOR");
					hasOR = true;
					regex = regex.substring(1);
				}
				// the POSITIVE CLOSURE case (e.g. a"+" )
				else if(regex.charAt(0) == POSITIVE_CLOSURE) {
					to.setTransition(STR_EPSILON , from);
					regex = regex.substring(1);
				}
				// the ASTERISK CLOSURE case (e.g. a"*" )
				else if(regex.charAt(0) == ASTERISK_CLOSURE) {
					to.setTransition(STR_EPSILON , from);
					from.setTransition(STR_EPSILON, to);
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
						
//						System.out.println("TRYING here");
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
//						System.out.println("REGEX: " + regex);
//						System.out.println("TRANS: " + transitionInput);
//						System.out.println("LEN OF TRANSITION OUTPUT:");
						regex = regex.substring(transitionInput.length());
					}else {
						transitionInput = regex.charAt(0) + "";
						regex = regex.substring(1);
					}
					
					if(!hasOR) {
						epsilonStart = new State("s" + rootState + state);
						if(epsilonStart != null && epsilonEnd != null) {
							epsilonEnd.setTransition(STR_EPSILON, epsilonStart);
						}
						// System.out.println("CREATING EPSILON STATES");
					}
					
					from = new State("q" + rootState + state + "0");
					to = new State("q" + rootState + state + "1");
					from.setTransition(transitionInput, to);
					
					epsilonStart.setTransition(STR_EPSILON, from);
					
					if(!hasOR) {
						epsilonEnd = new State("e" + rootState + state);
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

		return stateList;
	}
	
	public static void printStateTransitions(StateList stateList) {
		System.out.print("STARTING STATE  : ");
		ArrayList<Integer> starting = stateList.getStartingStateIndices();
		for(int i = 0; i < starting.size(); i++) {
			System.out.print(stateList.get(starting.get(i)).name + " ");
		}
		System.out.println();
		
		System.out.print("ACCEPTING STATE : ");
		ArrayList<Integer> accepting = stateList.getAcceptingStateIndex();
		for(int i = 0; i < accepting.size(); i++) {
			System.out.print(stateList.get(accepting.get(i)).name + " ");
		}
		System.out.println();
		
		System.out.println("TOTAL STATES    : " + stateList.size());
		
		// PRINT OUT THE STATE
		for(int i = 0; i < stateList.size(); i++) {
			String currState = stateList.get(i).name;
			
			System.out.println(currState);
			Map<String, StateList> currMap = stateList.get(i).connectedStates;
			if(currMap.keySet().size() > 0) {
				for(String key: currMap.keySet()) {
					
					StateList transitionOutput = currMap.get(key);
					for(int j= 0; j < transitionOutput.size(); j++) {					
						System.out.println("(" + currState + ", " + key + ") = " + transitionOutput.get(j).name);
					}
				}				
			}else {
				System.out.println("No Connections");
			}
			
			System.out.println();
			
		}
		
		
		
	}
	

	public static String addParenthesisPrecedences(String input) {
		input = addPadding(input, OR_OPERATOR);
		return input; 	
	}
	
	public static String addPadding(String input, char search) {
		boolean done = false;
		boolean found = false;

		int i = 0;
		int startingIndex = 0;
		
		while(!done) {
			if(i < input.length()) {					
				char currChar = input.charAt(i);
//				System.out.println("checking " + currChar);
//				System.out.println("REMAINING WITH CHECKING " + input.substring(i, input.length()));
				if(currChar == search) {					
					startingIndex = i;
					found = true;
				}
				i++;
			}else {
	//			System.out.println("CHECKED ALL");
				done = true;
				break;
			}
			
			if(found) {
		
//				System.out.println("FOUND AT INDEX " + startingIndex);
				ArrayList<Character> balanceStack;
				boolean closed;
				int leftIndex;
				int rightIndex;
				
				/****************************
				 * LEFT SIDE ****************
				 ****************************/
				leftIndex = startingIndex;		
				input = input.substring(0, leftIndex) + ")" + input.substring(leftIndex, input.length());
//				System.out.println("OUTPUT IS NOW: " + input);
				closed = false;
				balanceStack = new ArrayList<Character>();
				
				leftIndex--; // since ')' is added
				while(!closed) {
//					System.out.println("LEFTINDEX IS NOW " + leftIndex);
					if(leftIndex > -1) {
						char currRead = input.charAt(leftIndex); 
						if(currRead == '(') {
							if(balanceStack.size() > 0) {
								balanceStack.remove(0);
							}else{
								input = input.substring(0, leftIndex+1) + "(" + input.substring(leftIndex+1, input.length());
								closed = true;
							}
						}else if(currRead ==')') {
							balanceStack.add(')');
						}else if(currRead == search) {
							if(balanceStack.size() == 0) {
								input = input.substring(0, leftIndex+1) + "(" + input.substring(leftIndex+1, input.length());
								closed = true;
							}
						}
						leftIndex--;					
					}else {
						input = "(" + input;
						closed = true;
					}
				}
				
				startingIndex += 2;
				/****************************
				 * RIGHT SIDE ***************
				 ****************************/
				rightIndex = startingIndex;
				rightIndex++;
//				System.out.println("CURR SYMBOL IS " + input.charAt(rightIndex));
//				System.out.println("CHECKING " + input.substring(0, rightIndex));
//				System.out.println("CHECKING " + input.substring(rightIndex, input.length()));

				input = input.substring(0, rightIndex) + "(" + input.substring(rightIndex, input.length());
//				System.out.println("OUTPUT IS NOW: " + input);
				closed = false;
				balanceStack = new ArrayList<Character>();
				
				rightIndex++; // since '(' is added
//				System.out.println();
//				System.out.println("AFTER ADDING");
//				System.out.println("CURR SYMBOL IS " + input.charAt(rightIndex));
//				System.out.println("CHECKING " + input.substring(0, rightIndex));
//				System.out.println("CHECKING " + input.substring(rightIndex, input.length()));

//				closed = false;
				while(!closed) {
//					System.out.println("RIGHTINDEX IS NOW " + rightIndex);
					if(rightIndex < input.length()) {
						char currRead = input.charAt(rightIndex); 
//						System.out.println("\t\t\t" + currRead);
						if(currRead == ')') {
							if(balanceStack.size() > 0) {
								balanceStack.remove(0);
							}else{
								input = input.substring(0, rightIndex) + ")" + input.substring(rightIndex, input.length());
								closed = true;
							}
						}else if(currRead =='(') {
							balanceStack.add('(');
						}else if(currRead == search){
//							System.out.println("FOUND THE | AT INDEX " + rightIndex);
							if(balanceStack.size() == 0) {
								input = input.substring(0, rightIndex) + ")" + input.substring(rightIndex, input.length());
								closed = true;
							}
						}
						rightIndex++;					
					}else {
//						System.out.println("NEED TO DEFAULT NA");
						input =  input + ")";
						closed = true;
					}
				}
				
				i = startingIndex+1;
				found = false;
				
//				System.out.print("RESULT: ");
//				System.out.println(input);
//				System.out.print("LEFT  : ");
//				System.out.println(input.substring(0, i-1));
//				System.out.print("RIGHT : ");
//				System.out.println(input.substring(i, input.length()));
//				System.out.println();
//				System.out.println("CURRENLTY LOOKING AT: " + i + " ('" + input.charAt(i) + "')");
				
			}
		}
		return input;
	}
	
}

//(0|1)*1(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)

//(0|1)*1(0|1)

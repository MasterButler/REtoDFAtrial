package state;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

//((ab)|cd)ef|ghi
//( (ab) | cd ) ef | ghi

public class StateConstructor {
	
	public static char NULL_SYMBOL = 'Ø';
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
		
		try {
			String toCheck = regexInput;
			toCheck = toCheck.replaceAll("\\(", "");
			toCheck = toCheck.replaceAll("\\)", "");
//			System.out.println(toCheck);
//			Pattern p = Pattern.compile("([a-zA-Z0-9|\\s|+*?,.~-]|\\[|\\])*");
			Pattern p = Pattern.compile("([a-zA-Z0-9|\\s|$&+,:;=?@#\\\\|'\"<>.^*()/%!_{}\\-`~]|\\[|\\])*");
			if(!p.matcher(toCheck).matches()) {
				System.out.println("You have invalid input characters in your string.");
				return null;
			}
			
			StateList stateList = new StateList();
			
			String unedited = regexInput;
			if(toCheck.equals("")) {
				System.out.println("Blank input");
				State forBlank = new State("s0");
				forBlank.isAccepting = true;
				forBlank.setTransition(STR_EPSILON, forBlank);
				
				stateList.add(forBlank);
				printTransitionTable(stateList, unedited);
			}else {
//				System.out.println("ADDING PRECEDENCE MARKERS TO REGEX");
				regexInput = addParenthesisPrecedences(regexInput);
				regexInput = regexInput.replaceAll(" ", "[s]");
				System.out.println();
				System.out.println("STEP 0: IDENTIFY REGEX (" + regexInput + ")");
				
				NanoTimer timer = new NanoTimer();
				
				System.out.println("STEP 1: REGEX TO ENFA");
				timer.start();
				StateList eNFA = REtoENFA(regexInput);
				timer.stop();
				
				System.out.println("RE to eNFA took " + timer.getFormattedTimeLapsed());
				System.out.println("eNFA state count: " + eNFA.size());
				timer.reset();
//				System.out.println("============================");
//				System.out.println("EPSILON NFA STATE TRANSITION");
//				System.out.println("============================");
//				printStateTransitions(eNFA);
//				System.out.println("ENFA DONE (" + eNFA.size() + " states)");
//				System.out.println();
				
				if(eNFA == null) {
					return null;
				}
				
				System.out.println("STEP 2: ENFA TO DFA");
				timer.start();
				StateList DFA = ENFAtoDFA(eNFA);
				for(int i = 0; i < eNFA.size(); i++) {
					eNFA.get(i).name = "q" + i;
				}			
				for(int i = 0; i < DFA.size(); i++) {
					DFA.get(i).name = "q" + i;
				}
				timer.stop();
				
				System.out.println("eNFA to DFA took " + timer.getFormattedTimeLapsed());
				System.out.println("DFA state count: " + DFA.size());
				timer.reset();
//				System.out.println("==============================");
//				System.out.println("DFA TRANSITION ===============");
//				System.out.println("==============================");
//				printStateTransitions(DFA);
//				System.out.println("DFA DONE (" + DFA.size() + " states)");
//				System.out.println();

				if(DFA == null) {
					return null;
				}
				
				System.out.println("STEP 3: DFA TO MINIMIZED DFA");
				timer.start();
				DFA = minimizeDFA(DFA);
				for(int i = 0; i < DFA.size(); i++) {
					DFA.get(i).name = "q" + i;
				}
				timer.stop();
				
				System.out.println("DFA minimization took " + timer.getFormattedTimeLapsed());
				timer.reset();
//				System.out.println("============================");
//				System.out.println("MINIMIZED DFA TRANSITION");
//				System.out.println("============================");
//				printStateTransitions(DFA);
//				System.out.println("MINIMIZED DFA DONE (" + DFA.size() + " states)");
//				System.out.println();
				
				if(DFA == null) {
					return null;
				}
				
//				System.out.println("DFA produced " + DFA.size());

				System.out.println("============================");
				System.out.println("Minimized DFA (" + DFA.size() + " states)");
				System.out.println("============================");
				
				printTransitionTable(DFA, unedited);
				
				stateList =  DFA;
			}
			
			return stateList;
			
		}catch(Exception e) {
			
			System.out.println("Error: wrong input. Displaying error for more details");
			e.printStackTrace();
			
		}
		return null;
	}
	//a|ba|b
	public static StateList minimizeDFA(StateList stateList) {
		ArrayList<StateList> equivalence_0 = new ArrayList<StateList>();
		ArrayList<StateList> equivalence_1 = new ArrayList<StateList>();
		
		StateList accepting = stateList.getAcceptingStates();	
		StateList nonAccepting = stateList.getNonAcceptingStates();
		
		equivalence_0.add(accepting);
		equivalence_0.add(nonAccepting);
		
		equivalence_1 = equivalence_0;
		
		boolean equivalent = false;
		while(!equivalent) {
			equivalent = true;
			
//			System.out.println("=============================");
//			System.out.println("== NEW ITERATION ============");
//			System.out.println("=============================");
//			System.out.println("EQUIVALENCE 0");
//			for(int i = 0; i < equivalence_0.size(); i++) {
//				System.out.print(":");
//				for(int j = 0; j < equivalence_0.get(i).size(); j++) {
//					System.out.print(equivalence_0.get(i).get(j).name + "\t");
//				}
//				System.out.println();
//			}
//			
//			System.out.println("EQUIVALENCE 1");
//			for(int i = 0; i < equivalence_1.size(); i++) {
//				System.out.print(":");
//				for(int j = 0; j < equivalence_1.get(i).size(); j++) {
//					System.out.print(equivalence_1.get(i).get(j).name + "\t");
//				}
//				System.out.println();
//			}
			
			equivalence_0 = equivalence_1;
			equivalence_1 = new ArrayList<StateList>();	
			
			for(int i = 0; i < stateList.size(); i++) {
				if(i == 0) {
					equivalence_1.add(new StateList());
					equivalence_1.get(0).add(stateList.get(i));
				}else {
					State toCheck = stateList.get(i);
//					System.out.println("CHECKING STATE " + toCheck.name);
					for(int j = 0; j < equivalence_1.size(); j++) {
						State toCompare = equivalence_1.get(j).get(0);
//						System.out.println("\tTO: " + toCompare.name);
						Set<String> keySets = new HashSet<String>();
						keySets.addAll(toCheck.connectedStates.keySet());
						keySets.addAll(toCompare.connectedStates.keySet());
						
						boolean different = false;
						String similarWith = toCompare.name;
						if(toCheck.isAccepting == toCompare.isAccepting) {
							for(String key: keySets) {
								
//							System.out.println("\t==========\n\tTRANSITION: " + key);
								// hardcoded 0 since it's a dfa
								String resultingStateName = toCheck.getTransition(key).size() != 0 ?  
										toCheck.getTransition(key).get(0).name : "";
										int resultingIndex = -1;
										for(int k = 0; k < equivalence_0.size(); k++) {
											if(equivalence_0.get(k).isExisting(resultingStateName)) {
												resultingIndex = k;
											}
										}
										
										String comparingStateName = toCompare.getTransition(key).size() != 0 ? 
												toCompare.getTransition(key).get(0).name : "";					
												int comparingIndex = -1;
												for(int k = 0; k < equivalence_0.size(); k++) {
													if(equivalence_0.get(k).isExisting(comparingStateName)) {
														comparingIndex = k;
													}
												}
//											System.out.println();
//											System.out.println("\tRESULTING: " + resultingIndex);
//											System.out.println("\tCOMPARING: " + comparingIndex);
//											System.out.println();
												if(resultingIndex != comparingIndex) {
													different = true;
													similarWith = "";
													break;
												}
												
							}
						}else {
							different = true;
						}
						
						if(different) {
//						System.out.println("DIFFERENT");
							if(j == equivalence_1.size()-1) {
//								System.out.println("ADDING " + toCheck.name + " TO A NEW LIST\n");
//							System.out.println("DIFFERENT, ADDING A NEW ONE");
								StateList toAdd = new StateList();
								toAdd.add(toCheck);
								equivalence_1.add(toAdd);
								break;
							}
						}else {
							for(int l = 0; l < equivalence_1.size(); l++) {
								if(equivalence_1.get(l).isExisting(similarWith)) {
									equivalence_1.get(l).add(toCheck);
//									System.out.println("SAME, PLACED IN LAYER " + l);
									break;
								}
							}
						}
						if(!different) {
							break;
						}
					}
//					System.out.println();
//					System.out.println("CURRENT EQUIVALENCE 1: ");
//					System.out.println("EQUIVALENCE 1");
//					for(int m = 0; m < equivalence_1.size(); m++) {
//						System.out.print(":");
//						for(int n = 0; n < equivalence_1.get(m).size(); n++) {
//							System.out.print(equivalence_1.get(m).get(n).name + "\t");
//						}
//						System.out.println();
//					}
//					System.out.println();
				}
				
			}
			
			/***************************************
			 * check if equivalences are the same
			 ***************************************/
//			System.out.println("CHECK EQUIVALENCES");
			if(equivalence_0.size() == equivalence_1.size()) {
				for(int i = 0; i < equivalence_0.size(); i++) {
//					System.out.println("COMPARE SIZES OF ");
//					System.out.println();
					if( (equivalence_0.get(i).containsAll( equivalence_1.get(i) ) == false
							|| equivalence_1.get(i).containsAll( equivalence_0.get(i) ) == false ) ) {
						equivalent = false;
//						System.out.println("STILL NOT THE SAME");
						break;
					}
						
				}
			}else {
				equivalent = false;
			}
		}
		
//		System.out.println("GOT AN EQUIVALENT COMBINATION");
//		System.out.println("EQUIVALENCE 0");
//		for(int i = 0; i < equivalence_0.size(); i++) {
//			System.out.print(":");
//			for(int j = 0; j < equivalence_0.get(i).size(); j++) {
//				System.out.print(equivalence_0.get(i).get(j).name + "\t");
//			}
//			System.out.println();
//		}
//		
//		System.out.println("");
//		System.out.println("FINAL RESULTING EQUIVALENCE");
//		for(int i = 0; i < equivalence_1.size(); i++) {
//			System.out.print(":");
//			for(int j = 0; j < equivalence_1.get(i).size(); j++) {
//				System.out.print(equivalence_1.get(i).get(j).name + "\t");
//			}
//			System.out.println();
//		}
		
		
//		return stateList; 
		
		//convert ArrayList<StateList> to stateGroups
		StateList groupedList = new StateList();
		for(int i = 0; i < equivalence_1.size(); i++) {
			StateGroup toConvert = new StateGroup(equivalence_1.get(i));
			groupedList.add(toConvert);
		}
//		System.out.println("GROUPED_LIST SIZE IS " + groupedList.size());
//		for(int i = 0; i < groupedList.size(); i++) {
//			System.out.println(groupedList.get(i).name);
//		}
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
						String connectedStateName = toConnect.usedStates.predictSingleConnection(keySets.get(i));
//						System.out.println("SEARCHING FOR UNIT WITH NAME " + connectedStateName);
						int connectedStateIndex = groupedList.getIndexOfNearestExisting(connectedStateName);
						StateGroup connectedState = null;
						if(connectedStateIndex == -1) {
							System.out.println("CREATING NEW");
//							groupedList.createNewState();
//							System.out.println("heh");
							StateGroup newConnection = toConnect.usedStates.createNewConnection(keySets.get(i));
							groupedList.add(newConnection);
							connectedState = newConnection;
						}else {
							connectedState = (StateGroup)groupedList.get(connectedStateIndex);							
						}
//						System.out.println(toConnect.name + " goes to " + connectedState.name);
						toTraverse.add(connectedState);
						finalList.add(connectedState);
						toConnect.setTransition(keySets.get(i), connectedState);
//						System.out.println("(" + groupedList.get(i).name + ", " + keySets.get(i) + ") = " + "E(" + connectedStateName + ")");
						// 
					}
				}
//				System.out.println();
			}
			toConnect.hasBeenTraversed = true;
		}
		


		
		return finalList;
	}
	
	public static StateList ENFAtoDFA(StateList stateList) {		
		// Generate E-Closures of Each for easy indexing later on.
//		System.out.println("EPSILON CLOSURES");
		StateList groupedList = new StateList();
		for(int i = 0; i < stateList.size(); i++) {
			StateList eClosure = stateList.get(i).generateEClosures();
//			System.out.print("E(" + stateList.get(i).name + ")\t= ");
//			for(int j = 0; j < eClosure.size(); j++) {
//				System.out.print(eClosure.get(j).name + " ");
//			}
//			System.out.println();
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
		
//		System.out.println("============================");
//		System.out.println("DFA STATE TRANSITION");
//		System.out.println("============================");
//		printStateTransitions(finalList);
		
		return finalList;
	}
	
	public static StateList REtoENFA(String regexInput) {
//		System.out.println();
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
					System.out.println("i is: " + i);
					System.out.println("statelist size is: " + stateList.size());
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
						System.out.println("Now checking " + key.substring(1, key.length()-1));
						if(key.substring(1, key.length()-1).equals("")) {							
							StateList newStates = construct( key.substring(1, key.length()-1), rootStateNumber ); 
							
//						System.out.println("REROUTING");
//						printStateTransitions(newStates);						
							stateList.addAll(newStates);
//						System.out.println("END OF NEW STATE");
							
							
							// connect to start of new set
							stateList.get(i).setTransition(STR_EPSILON, newStates.get(newStates.getStartingStateIndex()));
							// connect to end of new set
							newStates.get(newStates.getEndingStateIndex()).setTransition(STR_EPSILON, stateList.get(i).getTransition(key));
							
							// sever connection of previously connected complicated nodes
							stateList.get(i).removeTransitionWithInputOf(key);
							
						}
						// restart the check
						i = 0;
						isAllChecked = false;
						break;
					}
				}
				
				i++;
				
			}
		}catch(Exception e) {
			System.out.println("ERROR DISCOVERED");
//			e.printStackTrace();
			return null;
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
			System.out.println("NOW READING: " + regex);
			while(!regex.isEmpty()) {
				System.out.println("Remaining: '" + regex + "'");
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
					System.out.println("FIRST ALPHA NUMERIC");
					System.out.println(regex);
					if(regex.charAt(0) == '\\'){
						regex = regex.substring(1);
						transitionInput = regex.charAt(0) + "";
						regex = regex.substring(1);
					}else if(regex.charAt(0) == '(') {
						LinkedList<Character> inputStack = new LinkedList<Character>();
						
//						System.out.println("TRYING here");
						String toCheck = regex;
						
						transitionInput = "";
//						transitionInput = toCheck.charAt(0) + "";
						
//						inputStack.add(toCheck.charAt(0));
//						toCheck = toCheck.substring(1);
						
						do {
							System.out.println(toCheck);
//						while(inputStack.size() > 0) {
							
							if(toCheck.charAt(0) == '(') {
								inputStack.add(toCheck.charAt(0));
							}else if(toCheck.charAt(0) == ')') {
								inputStack.removeLast();
							}
							
							transitionInput += toCheck.charAt(0);
							toCheck = toCheck.substring(1);
							
						}while(inputStack.size() > 0);
						
						System.out.println("FORMED: " + transitionInput);
//						System.out.println("REGEX: " + regex);
//						System.out.println("TRANS: " + transitionInput);
//						System.out.println("LEN OF TRANSITION OUTPUT:");
						regex = regex.substring(transitionInput.length());
					}else if(regex.charAt(0) == '['){
						String groupedClass = "";
						
						while(groupedClass.endsWith("]") == false) {
							groupedClass += regex.charAt(0);
							regex = regex.substring(1);
						}
						transitionInput = groupedClass;
						System.out.println("REGEX IS NOW: " + regex);
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
					
					System.out.println("TO INTEPRET: " + transitionInput);
					ArrayList<String> transitionList = new ArrayList<String>();
			        while(transitionInput.length() > 0){
			            char toSet = transitionInput.charAt(0);
			            System.out.println("READING:" + toSet);
			            if(toSet == '['){
			                String sets = transitionInput.substring( 0, transitionInput.indexOf(']')+1 );
							

							if( transitionInput.contains("a-z") ) {
								transitionList.addAll(getLowerCaseSet());
							}
							if( transitionInput.contains("A-Z") ) {
								transitionList.addAll(getUpperCaseSet());
							}
							if( transitionInput.contains("0-9") ) {
								transitionList.addAll(getNumberSet());
							}
							if( transitionInput.contains("s")) {
								transitionList.add(" ");
							}

							System.out.println(transitionInput.indexOf(']'));
//							System.out.println("sets is now: " + sets);
//							System.out.println("REMOVING 0 to " + sets.length());
							transitionInput = transitionInput.substring(sets.length());
//							System.out.println("REMAINING (after []): " + transitionInput);
			            }else{
			            	transitionList.add(transitionInput);
			                transitionInput = transitionInput.substring(1);
			            }
			            
//			            System.out.println("REMAINING: " + transitionInput);
			        }
			        System.out.println("NOW CONVERTING INPUTS INTO LIST OF AVAILABLE TRANSITIONS");
					for(int tl = 0; tl < transitionList.size(); tl++) {
						from.setTransition(transitionList.get(tl), to);
					}

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
			System.out.println("ERROR DISCOVERED");
			e.printStackTrace();
			return null;
		}

		return stateList;
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


	public static void printTransitionTable(StateList stateList, String regexInput) {
		System.out.println("REGULAR EXPRESSION " + regexInput);
		
		String toPrint = "";
		ArrayList<String> keySets = stateList.getAllKeySets();
		
		toPrint += "      -----------";
		for(int i = 0 ; i < keySets.size(); i++) {
			toPrint += "--------";
		}
		toPrint+="\n";

//		toPrint += "      |         |";
//		for(int i = 0 ; i < keySets.size(); i++) {
//			if(i == keySets.size()-1) {
//				toPrint += "\t|";
//			}else {
//				toPrint += "\t";
//			}
//		}
//		toPrint+="\n";
		
		toPrint += "      |         |";
		for(int i = 0 ; i < keySets.size(); i++) {
			if(i == 0) {
				toPrint +="Inputs ";
				if(i == keySets.size()-1) {
					toPrint += "|";
				}else {
					toPrint += " ";
				}
			}else if(i == keySets.size()-1) {
				toPrint += "\t|";
			}else {
				toPrint += "\t";
			}
		}
		toPrint+="\n";
		
		toPrint += "      |State    |";
		for(int i = 0 ; i < keySets.size(); i++) {
			toPrint += "--------";				
		}
		toPrint+="\n";

		
		toPrint += "      |         |";
		for(int i = 0 ; i < keySets.size(); i++) {
			toPrint += keySets.get(i) + "\t|";
		}
		toPrint+="\n";
		
		toPrint += "      -----------";
		for(int i = 0 ; i < keySets.size(); i++) {
			toPrint += "--------";
		}
		toPrint+="\n";
		
		for(int i = 0; i < stateList.size(); i++) {
			State currState = stateList.get(i);
			if(currState.isStarting) {
				toPrint += " ->";
			}else {
				toPrint += "   ";
			}
			
			if(currState.isAccepting) {
				toPrint += " *";
			}else {
				toPrint += "  ";
			}
			toPrint += " |" + currState.name + "\t|";
			
			for(int j = 0; j < keySets.size(); j++) {
				String connection = currState.getTransition(keySets.get(j)).size() > 0 ? 
						currState.getTransition(keySets.get(j)).get(0).name 
						: String.valueOf(NULL_SYMBOL);
				toPrint += connection + "\t|";
			}
			toPrint += "\n";
		}
		
		toPrint += "      -----------";;
		for(int i = 0 ; i < keySets.size(); i++) {
			toPrint += "--------";
		}
		toPrint+="\n";
		
		
		System.out.println(toPrint);
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
		
		System.out.print("STARTING STATE  : ");
		for(int i = 0; i < starting.size(); i++) {
			System.out.print(stateList.get(starting.get(i)).name + " ");
		}
		System.out.println();
		
		System.out.print("ACCEPTING STATE : ");
		for(int i = 0; i < accepting.size(); i++) {
			System.out.print(stateList.get(accepting.get(i)).name + " ");
		}
		System.out.println();
		
		System.out.println("TOTAL STATES    : " + stateList.size());
		
		
	}

	public static ArrayList<String> getLowerCaseSet(){
		ArrayList<String> lowerSet = new ArrayList<>();
		for(char a = 'a'; a <= 'z'; a++) {
//			System.out.print(a);
			lowerSet.add(String.valueOf(a));
		}
		return lowerSet;
	}
	
	public static ArrayList<String> getUpperCaseSet(){
		ArrayList<String> upperSet = new ArrayList<>();
		for(char a = 'A'; a <= 'Z'; a++) { 	
//			System.out.print(a);
			upperSet.add(String.valueOf(a));
		}
		return upperSet;
	}
	
	public static ArrayList<String> getNumberSet(){
		ArrayList<String> numberSet = new ArrayList<>();
		for(char a = '0'; a <= '9'; a++) {
//			System.out.print(a);
			numberSet.add(String.valueOf(a));
		}
		return numberSet;
	}
	
}
//(0|1)*1(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)

//(0|1)*1(0|1)

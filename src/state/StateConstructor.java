package state;

import java.util.Map;

public class StateConstructor {
	public static char OR_OPERATOR = '|';
	public static char POSITIVE_CLOSURE = '+';
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
				
				// a | b cases
				if(regex.charAt(0) == OR_OPERATOR) {
					// retain the epsilonStart and epsilonEnd of previous state
					System.out.println("FOUND AN OR OPERATOR");
					hasOR = true;
					regex = regex.substring(1);
				}
				
				// a + b cases
				else if(regex.charAt(0) == POSITIVE_CLOSURE) {
					to.setTransition(transitionInput, from);
					regex = regex.substring(1);
				}
				
				else {
//					System.out.println("FIRST ALPHA NUMERIC");
					if(regex.charAt(0) == '(') {
						
						
					}else {
//						System.out.println("NOT IN PARENTHESIS");
						if(!hasOR) {
							if(epsilonStart != null && epsilonStart != null) {
								epsilonEnd.setTransition(STR_EPSILON, epsilonStart);
							}
//							System.out.println("CREATING EPSILON STATES");
							epsilonStart = new State("s" + state);
						}
						transitionInput = regex.charAt(0) + "";
						regex = regex.substring(1);
						
						from = new State("q" + state + "0");
						to = new State("q" + state + "1");
						from.setTransition(transitionInput, to);
						
						epsilonStart.setTransition(STR_EPSILON, from);
						
						if(!hasOR) {
							epsilonEnd = new State("e" + state);
						}
						
						to.setTransition(STR_EPSILON, epsilonEnd);
						state++;
						
					}
					
					stateList.add(epsilonStart);
					stateList.add(epsilonEnd);
					stateList.add(from);
					stateList.add(to);
					hasOR = false;
				}
				
				
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("TOTAL NUMBER OF STATES: " + stateList.size() + "\n");
		for(int i = 0; i < stateList.size(); i++) {
			String currState = stateList.get(i).name;
			
			Map<String, State> currMap = stateList.get(i).connectedStates;
			for(String key: currMap.keySet()) {
				System.out.println("(" + currState + ", " + key + ") = " + currMap.get(key).name);
			}
			
			System.out.println();
		}
	}
}

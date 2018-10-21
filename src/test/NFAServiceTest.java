package test;


import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import state.StateConstructor;
import state.StateList;

public class NFAServiceTest {
	
	List<TestCase> tests = new ArrayList<>();
	
	public NFAServiceTest() {
		Scanner sc = new Scanner(System.in);
		Boolean isValid = true;
		
		System.out.print("Enter test case");
		while (isValid) 
        { 
			String reg = sc.nextLine();
            if(reg.equals("done"))
            	isValid = false;
            else
            	tests.add(new TestCase(reg));
        }
		
		System.out.print("Enter expected number of states");
		
		
		for (int i =0; i<tests.size(); i++) {
			int states = sc.nextInt();
			tests.get(i).setNumStatesNFA(states);
			
		}
		System.out.println("tests size: "+ tests.size());
		
	}
	
	
	public void CheckStates(){
		List<String> results = new ArrayList<>();
		
		for (int i =0; i<tests.size(); i++) {
			StateList stateList = new StateList();
			stateList = StateConstructor.REtoDFA(tests.get(i).getRegex());
			
			if (stateList.size() == tests.get(i).getNumStatesNFA()) {
				results.add("PASS i : " + (i+1) + "   states predicted: " + stateList.size() + "   actual states: " + tests.get(i).getNumStatesNFA());
			}
			
			else{
				results.add("FAIL i : " + (i+1) + "   states predicted: " + stateList.size() + "   actual states: " + tests.get(i).getNumStatesNFA());
			}
		}	
		
		for (int i =0; i<results.size(); i++) { 
			System.out.println(results.get(i));
		}
		
		
	}
	
}

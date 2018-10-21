package state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StateList extends ArrayList<State>{
	private static final long serialVersionUID = 1L;

	public boolean isExisting(String name) {
		for (State currState: this){
			if(currState.name.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isExisting(State state) {
		return isExisting(state.name);
	}
	
	public int getIndexIfExisting(State state){
		for(State currState: this) {
			if(currState.name.equals(state.name)) {
				return indexOf(currState);
			}
		}
		return -1;
	}
	
	public int getIndexIfExisting(String name) {
		for (State currState: this){
			if(currState.name.equals(name)) {
				return indexOf(currState);
			}
		}
		return -1;
	}
	
	public boolean add(State state) {
		if(!isExisting(state)) {
			return super.add(state);
		}
		return false;
	}
	
	public boolean addAll(StateList stateList) {
//		System.out.println("IN ADD ALL WITH STATE LIST COUTN OF " + stateList.size());
		for(State currState: stateList) {
//			System.out.println(currState.name);
			if(!isExisting(currState)) {
				super.add(currState);
			}
		}
		return true;
	}
	
	public int getStartingStateIndex() {
		StateList startState = new StateList();
		for(State currState: this) {
			if(currState.name.startsWith("s")){
				startState.add(currState);
			}
		}
		return indexOf(startState.get(0));
	}
	
	public ArrayList<Integer> getStartingStateIndices() {
		ArrayList<Integer> startingStateIndices = new ArrayList<Integer>();
		
		for (State currState: this){
			if(currState.name.contains("s0")) {
				startingStateIndices.add(indexOf(currState));
			}
		}
		return startingStateIndices;
	}
	
	public int getEndingStateIndex() {
		StateList endState = new StateList();
		for(State currState: this) {
			if(currState.name.startsWith("e")){
				endState.add(currState);
			}
		}
		return indexOf(endState.get(endState.size()-1));
	}
	
	public ArrayList<Integer> getAcceptingStateIndex() {
		ArrayList<Integer> acceptingStateIndices = new ArrayList<Integer>();
		
		for (State currState: this){
			if(currState.isAccepting == true) {
				acceptingStateIndices.add(indexOf(currState));
			}
		}
		return acceptingStateIndices;
	}
	
	public ArrayList<String> getAllKeySets(){
		Set<String> keySets = new HashSet<String>();
		for(State currState: this) {
			keySets.addAll(currState.connectedStates.keySet());
		}
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.addAll(keySets);
		return toReturn;
	}
	
	public String predictConnection(String transitionInput){
		
		StateList connections = new StateList();
		for(State currState: this) {
//			System.out.println("CHECKING " + currState.name + " WITH TRANSITION OF " + transitionInput);
			StateList toCheckList = currState.getTransition(transitionInput);
			for(State toCheck: toCheckList) {
				connections.addAll(toCheck.generateEClosures());	
			}
		}
		Collections.sort(connections);
		String name = "";
		for(int i = 0; i < connections.size(); i++) {
			name += connections.get(i).name;
		}
		return name;
	}
	
	public StateGroup createNewConnection(String transitionInput) {
		
		StateList connections = new StateList();
		for(State currState: this) {
//			System.out.println("CHECKING " + currState.name + " WITH TRANSITION OF " + transitionInput);
			StateList toCheckList = currState.getTransition(transitionInput);
			for(State toCheck: toCheckList) {
				connections.addAll(toCheck.generateEClosures());	
			}
		}
		Collections.sort(connections);
		String name = "";
		for(int i = 0; i < connections.size(); i++) {
			name += connections.get(i).name;
		}
		StateGroup newConnection = null;
		if(!isExisting(name)) {
//			System.out.println("OO NGA NO LOL");
			newConnection = new StateGroup(connections);
			return newConnection;
		}
		return null;
	}
}

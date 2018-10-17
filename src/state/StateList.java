package state;

import java.util.ArrayList;

public class StateList extends ArrayList<State>{
	private static final long serialVersionUID = 1L;

	public boolean isExisting(String name) {
		for (State currState: this){
			if(currState.name == name) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isExisting(State state) {
		return isExisting(state.name);
	}
	
	public boolean add(State state) {
		if(!isExisting(state)) {
			return super.add(state);
		}
		return false;
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
	
	public int getEndingStateIndex() {
		StateList endState = new StateList();
		for(State currState: this) {
			if(currState.name.startsWith("e")){
				endState.add(currState);
			}
		}
		return indexOf(endState.get(endState.size()-1));
	}
}

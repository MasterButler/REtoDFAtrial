package state;

import java.util.ArrayList;

public class StateList extends ArrayList<State>{
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
}

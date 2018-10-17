package state;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
	public String name;
	public Map<String, StateList> connectedStates;
	
	public State(String name) {
		this.name = name;
		this.connectedStates = new HashMap<String, StateList>();
	}
	
	public void setTransition(String transitionInput, State transitionState){
		StateList transitionOutput;
		if(connectedStates.get(transitionInput) == null) {
			transitionOutput = new StateList();
		}else {
			transitionOutput = getTransition(transitionInput);
		}
		transitionOutput.add(transitionState);
		connectedStates.put(transitionInput, transitionOutput);
	}
	
	public void setTransition(String transitionInput, StateList transitionStateList){
		connectedStates.put(transitionInput, transitionStateList);
	}
	
	
	public StateList getTransition(String transitionInput) {
		return connectedStates.get(transitionInput);
	}
	
	public StateList removeTransitionWithInputOf(String transitionInput) {
		return connectedStates.remove(transitionInput);
	}
}

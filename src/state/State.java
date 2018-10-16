package state;
import java.util.HashMap;
import java.util.Map;

public class State {
	public String name;
	public Map<String, State> connectedStates;
	
	public State(String name) {
		this.name = name;
		this.connectedStates = new HashMap<String, State>();
	}
	
	public void setTransition(String transitionInput, State transitionState){
		connectedStates.put(transitionInput, transitionState);
	}
	
	public State getTransition(String transitionInput) {
		return connectedStates.get(transitionInput);
	}
}

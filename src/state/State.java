package state;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
	public String name;
	public Map<String, StateList> connectedStates;
	public boolean isAccepting;
	public StateList epsilonClosures;
	
	public State(String name) {
		this.name = name;
		this.connectedStates = new HashMap<String, StateList>();
		this.isAccepting = false;
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
	
	public boolean setAccepting(boolean accepting) {
		this.isAccepting = accepting;
		return isAccepting;
	}
	
	public StateList getTransition(String transitionInput) {
		return connectedStates.get(transitionInput) != null ? connectedStates.get(transitionInput) : new StateList();
	}
	
	public StateList removeTransitionWithInputOf(String transitionInput) {
		return connectedStates.remove(transitionInput);
	}
	
	public StateList generateEClosures() {
		this.epsilonClosures = new StateList();
		
		StateList eClosures = new StateList();
		eClosures.add(this);
		eClosures.addAll(getTransition(StateConstructor.STR_EPSILON));
	
//		System.out.println("PRELIMINARY COUNT FOR E CLOSURES OF STATE " + this.name + ": " + eClosures.size());
//		System.out.println("FINAL COUNT FOR E CLOSURES OF STATE " + this.name + ": " + eClosures.size());
		
		for(int i = 0;i < eClosures.size(); i++) {
			if(eClosures.get(i).name == this.name) {
				continue;
			}else {
//				System.out.println("Now traversing: " + eClosures.get(i).name);
				eClosures.addAll(eClosures.get(i).getTransition(StateConstructor.STR_EPSILON));
			}				
		}

		this.epsilonClosures = eClosures;
		return this.epsilonClosures;
	}
}

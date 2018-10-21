package state;

import java.util.ArrayList;

public class StateGroup extends State{
	public boolean hasBeenTraversed;
	public StateList usedStates;
	
	public StateGroup(StateList toCollapse) {
		super();
		
		String name = "";
		for(int i = 0; i < toCollapse.size(); i++) {
			name += toCollapse.get(i).name;
			if(toCollapse.get(i).isAccepting == true) {
				this.isAccepting = true;
			}
		}
		this.name = name.trim();
		this.hasBeenTraversed = false;
		usedStates = toCollapse;
	}
	
}

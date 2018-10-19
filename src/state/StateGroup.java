package state;

public class StateGroup extends State{
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
	}
}

package state;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;
import static guru.nidi.graphviz.model.Factory.to;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.RankDir;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

public class State implements Comparable<State>{
	public String name;
	public Map<String, StateList> connectedStates;
	public boolean isStarting;
	public boolean isAccepting;
	public StateList epsilonClosures;
	
	protected State() {
		this.connectedStates = new HashMap<String, StateList>();
	}
	
	public State(String name) {
		this();
		this.name = name;
		this.isAccepting = false;
	}
	
    public int compareTo(State other) {
        return this.name.compareTo(other.name);
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
	
	public boolean setStarting(boolean starting) {
		this.isStarting = starting;
		return isStarting;
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
		Collections.sort(this.epsilonClosures);
		
		return this.epsilonClosures;
	}
	
	
	public MutableGraph getGraphVizGraph() {
		MutableNode a = mutNode(this.name);
		System.out.print(this.name);
		if(isAccepting) {
			a.add(Shape.DOUBLE_CIRCLE);
			System.out.println(": Acccepting");
		}else {
			a.add(Shape.CIRCLE);
			System.out.println(": Non-acccepting");
		}
		System.out.println();
		
		MutableGraph mutGraph = mutGraph(this.name).setDirected(true);		
		
//		StateList connections = new StateList();
//			connections.addAll(getTransition(key));			
		for(String key: connectedStates.keySet()) {
			System.out.println("Adding transition " + key);
			State currState = getTransition(key).get(0);
			MutableNode currNode = mutNode(currState.name);
			
			System.out.print(currState.name);
			if(currState.isAccepting) {			
				currNode.add(Shape.DOUBLE_CIRCLE);
				System.out.println(": Acccepting");
			}else {
				currNode.add(Shape.CIRCLE);
				System.out.println(": Non-acccepting");
			}
			
			String label = key;
			if(label.trim().length() == 0) {
				label = "<space>";
			}
			Graph g = graph(key).directed()
			        .graphAttr().with(RankDir.LEFT_TO_RIGHT)
			        .with(
			        	a.addLink(to(currNode).with(Label.of(label)))
			        );
			g.addTo(mutGraph);
			System.out.println();
		}
		
		Iterator<MutableGraph> it = mutGraph.graphs().iterator();
		while(it.hasNext()) {
			MutableGraph element = it.next();
			System.out.println(element.name());
			
			Iterator<MutableNode> mn = element.nodes().iterator();
			System.out.println("===");
			while(mn.hasNext()) {
				MutableNode elementNode = mn.next();
				System.out.println(elementNode.name());
				System.out.println("number links: " + elementNode.links().size());
			}
			System.out.println("===");
			System.out.println();
		}
		
		return mutGraph;
	}
}

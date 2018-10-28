import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import static guru.nidi.graphviz.model.Factory.*;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.RankDir;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
import state.State;
import state.StateConstructor;
import state.StateList;

public class Driver {
	
    /**
     * Load the config.properties file.
     */
    private final static String cfgProp = "src/config.properties";
    private final static Properties configFile = new Properties() {
        private final static long serialVersionUID = 1L; {
            try {
            	System.out.println(cfgProp);
                load(new FileInputStream(cfgProp));
            } catch (Exception e) { 
            	e.printStackTrace(); 
            }
        }
    };

	
	public static void main(String[] args) {
		
		String regexInput = forCheckingNumI();
		StateList finalList = StateConstructor.REtoDFA(regexInput);
		
		System.out.println("FINAL LIST: ");
		StateConstructor.printStateTransitions(finalList);
		
		MutableGraph minimized = mutGraph("Minimized DFA for " + regexInput).setDirected(true);
		
//		for(int i = 0; i < 1; i++) {
		for(int i = 0; i < finalList.size(); i++) {
			State currState = finalList.get(i);
			
			MutableGraph currGraph = currState.getGraphVizGraph();
			System.out.println("ADDING TO FINAL LIST PROPAGATED FROM " + finalList.get(i));
			currGraph.addTo(minimized);		
		}
		
		try {
			String curr = String.valueOf(System.currentTimeMillis());
			Graphviz.fromGraph(minimized).height(5000).render(Format.PNG).toFile(new File("output/" + curr + ".png"));
			System.out.println("DONE");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static String forCheckingNumI() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter case (~ for epsilon): ");
		return sc.nextLine();
	}
	
	public static String forCheckingNum0() {
		return "a*";
	}
	
	public static String forCheckingNum1() {
		return "(a|b)*abb";
	}
	
	public static String forCheckingNum2() {
		return "(0|1)*1(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)(0|1)";
	}
	
	public static String forCheckingNum3() {
		return "[a-z]?[a-z]?[a-z]?[a-z]?[a-z]?[a-z]?[a-z]?[a-z]?[a-z]?[a-z]?";
	}
	
	public static String forCheckingNum4() {
		return "(a+|~)";
	}
	
	public static String forCheckingNumWrong() {
		return "(a+|~!)";
	}
	
	public static void testPrecedence() {
		System.out.println("==================================");
		System.out.println("== CASE 1 ========================");
		System.out.println();
		System.out.println(StateConstructor.REtoDFA("ab|cde"));
		// must return (ab)|(cde)
		
		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 2A =======================");
		System.out.println();
		System.out.println(StateConstructor.REtoDFA("a(bc)|de"));
		// must return (a(bc))|(de)

		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 2B =======================");
		System.out.println();
		System.out.println(StateConstructor.REtoDFA("ab|(cd)e"));
		// must return (ab)|((cd)e)

		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 3 ========================");
		System.out.println();
		System.out.println(StateConstructor.REtoDFA("(ab)c|de"));
		// must return ((ab)c)|(de)
		
		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 4 ========================");
		System.out.println();
		System.out.println(StateConstructor.REtoDFA("a(b|c)de"));
		// must return a((b)|(c))de

		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 5 ========================");
		System.out.println();
		StateConstructor.REtoDFA("ab|cd|ef");
		// must return (ab)|(cd)|(ef)
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE FINAL A ==================");
		System.out.println();
		System.out.println(StateConstructor.REtoDFA("a((bc)*d|e)(f|g)|hi"));
		// must return (ab)|(cd)|(ef)
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE FINAL B ==================");
		System.out.println();
		System.out.println(StateConstructor.REtoDFA("a"));
		// must return (ab)|(cd)|(ef)
		
		
	}
}

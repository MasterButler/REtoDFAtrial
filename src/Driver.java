import static guru.nidi.graphviz.model.Factory.mutGraph;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JFileChooser;

import accepter.StateAccepter;
import filereader.TextFileReader;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import state.State;
import state.StateConstructor;
import state.StateList;

public class Driver {

	public static void main(String[] args) {

		/**************************************************
		 * SELECT THE FILE
		 *************************************************/
		String file = "";
		JFileChooser chooser = new JFileChooser();
		do {
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				chooser.getSelectedFile().getAbsolutePath();
				file = chooser.getSelectedFile().getAbsolutePath();
				break;
			}	    	
		}while(true);
		
		file = new File(file).getAbsolutePath();
		System.out.println("File to read: " +  file);
		String toCheck = TextFileReader.read(file);
		
		/**************************************************
		 * GENERATE THE DFA
		 *************************************************/
		String regexInput = forCheckingNumI();
		StateList finalList = StateConstructor.REtoDFA(regexInput);
//		try {
//			generateVisualization(regexInput, finalList);			
//		}catch(Exception e) {
//			System.out.println("Unable to generate visualization because of large size.");
//		}
		
		
		/**************************************************
		 * TEST THE FILE
		 *************************************************/
		int[][] acceptedSubstrings = StateAccepter.getAllAcceptingSubStrings(finalList, toCheck);
		System.out.println("\n\nLocation of strings accepted by regex " + regexInput);
		System.out.println();
		for(int i = 0; i < acceptedSubstrings.length; i++) {
			int startingIndex = acceptedSubstrings[i][0];
			int endingIndex = acceptedSubstrings[i][1];
			System.out.println(startingIndex + " to " + endingIndex);
			System.out.println(toCheck.substring(0, startingIndex) 
					+ "[" + toCheck.substring(startingIndex, endingIndex+1) + "]" 
					+ toCheck.substring(endingIndex+1));
			System.out.println();
		}
		System.out.println("-- End of list --");
	}
	
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
    
	public static void generateVisualization(String regexInput, StateList finalList) {
//		System.out.println("FINAL LIST: ");
//		StateConstructor.printStateTransitions(finalList);
		
		MutableGraph minimized = mutGraph("Minimized DFA for " + regexInput).setDirected(true);
		
//		for(int i = 0; i < 1; i++) {
		for(int i = 0; i < finalList.size(); i++) {
			State currState = finalList.get(i);
			
			MutableGraph currGraph = currState.getGraphVizGraph();
//			System.out.println("ADDING TO FINAL LIST PROPAGATED FROM " + finalList.get(i));
			currGraph.addTo(minimized);		
		}
		
		try {
			String curr = String.valueOf(System.currentTimeMillis());
			Graphviz.fromGraph(minimized).height(5000).render(Format.PNG).toFile(new File("output/" + curr + ".png"));
//			System.out.println("DONE");
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

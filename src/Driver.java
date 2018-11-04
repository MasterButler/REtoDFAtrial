import static guru.nidi.graphviz.model.Factory.mutGraph;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import accepter.StateAccepter;
import filereader.TextFileReader;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import state.State;
import state.StateConstructor;
import state.StateList;

public class Driver {

	public static String selectFile() {
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
		
		return TextFileReader.read(file);
	}
	
	
	public static void main(String[] args) {

		/**************************************************
		 * SELECT THE FILE
		 *************************************************/
		String toCheck = selectFile();
		
		/**************************************************
		 * GENERATE THE DFA
		 *************************************************/
		String regexInput = forCheckingNumI();
		StateList finalList = StateConstructor.REtoDFA(regexInput);
		try {
			String filename = generateVisualization(regexInput, finalList);
			
			JFrame frame = new JFrame();
			ImageIcon icon = new ImageIcon(filename);
			JLabel label = new JLabel(icon);
			frame.add(label);
			frame.setTitle("DFA for regular expression " + regexInput);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setBackground(Color.WHITE);
			frame.pack();
			frame.setVisible(true);
		}catch(Exception e) {
			System.out.println("Unable to generate visualization because of large size.");
		}
		
		/**************************************************
		 * TEST THE FILE
		 *************************************************/
		int[][] acceptedSubstrings = StateAccepter.getAllAcceptingSubStrings(finalList, toCheck);
		if(acceptedSubstrings.length > 0) {			
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
		}else {
			System.out.println("No accepted subsequences in string.");
		}
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
    
	public static String generateVisualization(String regexInput, StateList finalList) {
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
			String filename = "output/" + curr + ".png";
			Graphviz.fromGraph(minimized).height(800).render(Format.PNG).toFile(new File(filename));
//			System.out.println("DONE");
			return filename;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
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

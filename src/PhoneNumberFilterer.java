import static guru.nidi.graphviz.model.Factory.mutGraph;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import accepter.StateAccepter;
import filereader.TextFileReader;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import state.State;
import state.StateConstructor;
import state.StateList;

public class PhoneNumberFilterer {
	public static String PHONE_NUMBER_REGEX = "(\\+?639[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])|"
			+ "(09[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])";
	public static void start(String toCheck){
//		String regexInput = "(\\+63( )?9[0-9][0-9][0-9]( )?[0-9][0-9][0-9][0-9][0-9][0-9])"
//						 + "|(09[0-9][0-9][0-9]( )?[0-9][0-9][0-9][0-9][0-9][0-9])"
//						 + "|(\\+63-?9[0-9][0-9][0-9]-?[0-9][0-9][0-9][0-9][0-9][0-9])"
//						 + "|(09[0-9][0-9][0-9]-?[0-9][0-9][0-9][0-9][0-9][0-9])";
		
		String regexInput = PHONE_NUMBER_REGEX;
		StateList finalList = StateConstructor.REtoDFA(regexInput);

		/**************************************************
		 * GENERATE THE DFA
		 *************************************************/
		try {
			String filename = generateVisualization(regexInput, finalList);
			JFrame frame = new JFrame();
			
			
			ImageIcon icon = new ImageIcon(filename);
			JLabel label = new JLabel(icon);
			
			JPanel figPanel = new JPanel();
			figPanel.add(label);
			figPanel.setBackground(Color.WHITE);
			
			JScrollPane scroller = new JScrollPane(figPanel);
			scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			frame.add(scroller);
			frame.setTitle("DFA for regular expression " + regexInput);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setBackground(Color.WHITE);
			frame.pack();
			frame.setVisible(true);
		}catch(Exception e) {
			System.out.println("Unable to generate visualization because of large size.");
		}
		
		if(finalList != null) {			
			/**************************************************
			 * TEST THE FILE
			 *************************************************/
			int[][] acceptedSubstrings = StateAccepter.getAllAcceptingSubStrings(finalList, toCheck);
			if(acceptedSubstrings.length > 0) {			
				System.out.println("\n\nLocation of strings accepted by regex " + regexInput);
				System.out.println();
				if(toCheck.length() == 0) {
					int startingIndex = acceptedSubstrings[0][0];
					int endingIndex = acceptedSubstrings[0][1];
					System.out.println(startingIndex + " to " + endingIndex);
					System.out.println("Empty text file was accepted.");
				}else {
					for(int i = 0; i < acceptedSubstrings.length; i++) {
						int startingIndex = acceptedSubstrings[i][0];
						int endingIndex = acceptedSubstrings[i][1];
						System.out.println(startingIndex + " to " + endingIndex);
						System.out.println("[" + toCheck.substring(startingIndex, endingIndex+1) + "]");
						System.out.println();
					}	
				}
				System.out.println("Total found:" + acceptedSubstrings.length);
				System.out.println("-- End of list --");
			}else {
				System.out.println("No accepted subsequences in string.");
			}
		}
	}
	
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
		
		return selectFile(file);
	}
	
	public static String selectFile(String file) {
		file = new File(file).getAbsolutePath();
		System.out.println("File to read: " +  file);
		
		return TextFileReader.read(file);
	}
	
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
	
}

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import state.StateConstructor;

public class Driver {
	
	public static String forCheckingNumI() {
		Scanner sc = new Scanner(System.in);
		
		return sc.nextLine();
	}
	
	public static String forCheckingNum0() {
		return "a*";
	}
	
	public static String forCheckingNum1() {
		return "(a|b)*abb";
	}
	
	public static void main(String[] args) {
		
		System.out.print("Enter case: " );
		String regexInput = forCheckingNum0();
		
		StateConstructor.REtoDFA(regexInput);
		System.out.println("DONE");
		
//		ArrayList<Integer> a = new ArrayList<>();
//		ArrayList<Integer> b = new ArrayList<>();
//		a.add(5);
//		a.add(2);
//		a.add(3);
//		
//		b.add(5);
//		b.add(3);
//		b.add(2);
//		
//		if(a.containsAll(b) && b.containsAll(a)) {
//			System.out.println("SAME");
//		}else {
//			System.out.println("DIFFERENT");
//		}
	}
}

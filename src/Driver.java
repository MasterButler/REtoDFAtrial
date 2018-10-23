import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import state.StateConstructor;

public class Driver {
	
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
	
	public static void main(String[] args) {
		
		String regexInput = forCheckingNumI();
		StateConstructor.REtoDFA(regexInput);
		
//		testPrecedence();
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import state.StateConstructor;

public class Driver {
	
	public static String forCheckingNumI() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter case: " );
		return sc.nextLine();
	}
	
	public static String forCheckingNum0() {
		return "a*";
	}
	
	public static String forCheckingNum1() {
		return "(a|b)*abb";
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
		System.out.println(StateConstructor.addParenthesisPrecedences("ab|cde"));
		// must return (ab)|(cde)
		
		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 2A =======================");
		System.out.println();
		System.out.println(StateConstructor.addParenthesisPrecedences("a(bc)|de"));
		// must return (a(bc))|(de)

		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 2B =======================");
		System.out.println();
		System.out.println(StateConstructor.addParenthesisPrecedences("ab|(cd)e"));
		// must return (ab)|((cd)e)

		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 3 ========================");
		System.out.println();
		System.out.println(StateConstructor.addParenthesisPrecedences("(ab)c|de"));
		// must return ((ab)c)|(de)
		
		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 4 ========================");
		System.out.println();
		System.out.println(StateConstructor.addParenthesisPrecedences("a(b|c)de"));
		// must return a((b)|(c))de

		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE 5 ========================");
		System.out.println();
		StateConstructor.addParenthesisPrecedences("ab|cd|ef");
		// must return (ab)|(cd)|(ef)
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE FINAL A ==================");
		System.out.println();
		System.out.println(StateConstructor.addParenthesisPrecedences("a((bc)*d|e)(f|g)|hi"));
		// must return (ab)|(cd)|(ef)
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("==================================");
		System.out.println("== CASE FINAL B ==================");
		System.out.println();
		System.out.println(StateConstructor.addParenthesisPrecedences("a"));
		// must return (ab)|(cd)|(ef)
		
		
	}
}

package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import state.StateConstructor;
import test.NFAServiceTest;

public class TestDriver {
	
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
		NFAServiceTest nfatest = new NFAServiceTest();
		nfatest.CheckSENFAtates();
//		nfatest.CheckDFAStates();
		
	}
}

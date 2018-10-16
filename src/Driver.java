import state.StateConstructor;

public class Driver {

	public static void main(String[] args) {
		
//		System.out.println("CASE: AB");
//		StateConstructor.construct("ab");		
//		System.out.println("CASE: A|B");
//		StateConstructor.construct("a|b");
		System.out.println("CASE: A|B");
//		StateConstructor.construct("(ab)|c");
		StateConstructor.construct("a?|b");
		
//		StateList a = new StateList();
//		State x = new State("A1");
//		State y = new State("B1");
//		State z = new State("A1");
//		a.add(x);
//		a.add(y);
//		a.add(z);
//		for(int i = 0; i < a.size(); i++) {
//			
//		}
		
//		System.out.println("TRIAL");
//		
//		String regex = "(a(b))c(d)e";
//		String transitionInput = "";
//		if(regex.charAt(0) == '(') {
//			LinkedList<Character> inputStack = new LinkedList<Character>();
//			
//			System.out.println("TRYING here");
//			String toCheck = regex;
//			
//			transitionInput += toCheck.charAt(0);
//			
//			inputStack.add(toCheck.charAt(0));
//			toCheck = toCheck.substring(1);
//			
//			while(inputStack.size() > 0) {
//				if(toCheck.charAt(0) == '(') {
//					inputStack.add(toCheck.charAt(0));
//				}else if(toCheck.charAt(0) == ')') {
//					inputStack.removeLast();
//				}
//				transitionInput += toCheck.charAt(0);
//				toCheck = toCheck.substring(1);
//			}
//		}
//		System.out.println(regex);
//		System.out.println(transitionInput);
		
	}

}

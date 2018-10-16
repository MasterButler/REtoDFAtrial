import state.StateConstructor;

public class Driver {

	public static void main(String[] args) {
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
//		System.out.println("CASE: AB");
//		StateConstructor.construct("ab");
		
		System.out.println("CASE: A|B");
		StateConstructor.construct("a|b");
		
	}

}

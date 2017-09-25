package Ring;

import java.util.ArrayList;
import java.util.Random;

public class Hello {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int nodeid = 2;
		int fingertableSize=3;
		
		ArrayList<Integer> list = new ArrayList<>();
		list.add(1);
		
		for (int i = 1; i <= 5; i++) {
			// hashtable.put(i, arg1)
			
			//double succssorNODE =  nodeid + Math.pow(2,(i-1));
			//this.findTheSuccessor(succssorNODE);
			System.out.println(Math.pow(2,(i-1)));
		}
		
		
		Random rand = new Random();
		
		int x = rand.nextInt(list.size());
		System.out.println("size of the list :::::"+ list.size());
		System.out.println(x);
	}

}

import java.io.*;
import java.util.*;
//Philippe Bergeron
//260928589

public class main {
	
     
    public static void main(String[] args) {
    	//******************TESTER**************************
    	Open_Addressing hash = new Open_Addressing(10,0,-1);
    	int hashnum = hash.probe(1, 0);
    	System.out.println(hashnum);
    	System.out.println("-------LOLOL-----");
    	Chaining hashChain = new Chaining(10,15,15);
    	ArrayList<ArrayList<Integer>> size = hashChain.Table;
    	int slots = hashChain.m;
    	
    	Open_Addressing address = new Open_Addressing(10,17,-1);
    	int[] asize = address.Table;
    	int noslots = address.m;
    	
    	int newKey = 9;
    	
    	for (int i = 0; i<(noslots-2); i++) {
    		int hashNum = address.probe(newKey, 0);
    		System.out.print("HashValue: ");
    		System.out.println(hashNum);
    		System.out.println(newKey);
    		System.out.print("i = ");
    		System.out.println(i);
    		int collis = address.insertKey(newKey);
    		newKey += 2;
    		System.out.print("No of collisions: ");
    		System.out.println(collis);
    	}
    	System.out.println("---------------Insert Key----------");
    	
    	int in = address.insertKey(179);
    	System.out.println(in);
    	System.out.println("-----------Remove Key-------");
    	
    	int rem = address.removeKey(22);
    	System.out.println(rem);
    	
    	int rem1 = address.removeKey(12);
    	System.out.println(rem1);
    	
    	System.out.println("-----------No of slots--------");
    	
    	int[] keyArray = {0,14,32,56,85,34,59,23,41,54,71,87,9,38,6,3,89,76,56,34,23,57,1,2,5,10,12,16};
    	
    	int coll = address.insertKeyArray(keyArray);
    	int coll2 = hashChain.insertKeyArray(keyArray);
    	
    	System.out.println(slots);
    	System.out.println(noslots);
    	System.out.println(coll);
    	System.out.println(coll2);
    }
}
    	

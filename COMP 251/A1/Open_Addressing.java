import java.io.*;
import java.util.*;
//Philippe Bergeron
//260928589
public class Open_Addressing {
     public int m; // number of SLOTS AVAILABLE
     public int A; // the default random number
     int w;
     int r;
     public int[] Table;

     protected Open_Addressing(int w, int seed, int A) {

         this.w = w;
         this.r = (int) (w-1)/2 +1;
         this.m = power2(r);
         if (A==-1){
            this.A = generateRandom((int) power2(w-1), (int) power2(w),seed);
         }
        else{
            this.A = A;
        }
         this.Table = new int[m];
         for (int i =0; i<m; i++) {
             Table[i] = -1;
         }
         
     }
     
                 /** Calculate 2^w*/
     public static int power2(int w) {
         return (int) Math.pow(2, w);
     }
     public static int generateRandom(int min, int max, int seed) {     
         Random generator = new Random(); 
                 if(seed>=0){
                    generator.setSeed(seed);
                 }
         int i = generator.nextInt(max-min-1);
         return i+min+1;
     }
        /**Implements the hash function g(k)*/
        public int probe(int key, int i) {
        	
        	// Chain + i
        	int hashFunction = (A*key)%(power2(w)) >> (w-r);
            int hashNum = (hashFunction + i)%power2(r);
            
        return hashNum;
     }
     
     
     /**Inserts key k into hash table. Returns the number of collisions encountered*/
        public int insertKey(int key){
        	int collision;
        	collision = 0;
            int hashNum;

            for (int i=0; i< (m);i++) {
            	hashNum = probe(key,i);
            	if ((Table[hashNum] == -1) || (Table[hashNum] == -5)) { //We insert into a deleted or empty slot
            		Table[hashNum] = key;
            		return collision;
            	} else {
            		collision += 1; //An empty or deleted slot was not found
            						//Continue searching slot
            	}
            }
            
            return collision;  
        }
        
        /**Sequentially inserts a list of keys into the HashTable. Outputs total number of collisions */
        public int insertKeyArray (int[] keyArray){
            //TODO
            int collision = 1;
            for (int key: keyArray) {
                collision += insertKey(key);
            }
            return collision;
        }
            
         /**Inserts key k into hash table. Returns the number of collisions encountered*/
        public int removeKey(int key){
            int collision;
            collision = 0;
            int hashNum;
            
            for (int i=0; i<m;i++) {
            	hashNum = probe(key,i);
            	if (Table[hashNum] == key) { //The key was found
            		Table[hashNum] = -5; //A deleted node is given value of -5
            		return collision;
            	} else if (Table[hashNum] == -1){//Empty node not initialized
            		collision += 1; //The key is not found
            		return collision;
            	} else {
            		collision += 1; //Continue searching
            	}
            }
                
            return collision;
        }
}
package FinalProject_Template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
    // num of entries to the table
    private int numEntries;
    // num of buckets 
    private int numBuckets;
    // load factor needed to check for rehashing 
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K,V>>> buckets; 
    
    // constructor
    public MyHashTable(int initialCapacity) {
        // ADD YOUR CODE BELOW THIS
        this.numBuckets = initialCapacity;
        this.numEntries = 0;
        this.buckets = new ArrayList<LinkedList<HashPair<K,V>>>();
        //LinkedList<HashPair<K,V>> list = new LinkedList<HashPair<K,V>>();
        
        
        for (int i=0;i<initialCapacity;i++) {
        	buckets.add(new LinkedList<HashPair<K,V>>());
        }
        //ADD YOUR CODE ABOVE THIS
    }
    
    public int size() {
        return this.numEntries;
    }
    
    public boolean isEmpty() {
        return this.numEntries == 0;
    }
    
    public int numBuckets() {
        return this.numBuckets;
    }
    
    /**
     * Returns the buckets variable. Useful for testing  purposes.
     */
    public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){
        return this.buckets;
    }
    
    /**
     * Given a key, return the bucket position for the key. 
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.numBuckets;
        return hashValue;
    }
    
    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    public V put(K key, V value) {
       
    	// Gives index of key
    	int index = hashFunction(key);
    	
    	// Creates a HashPair object
    	HashPair<K, V> pair = new HashPair<K, V>(key, value);
    	
    	//Gives the linked list where to add the HashPair
    	LinkedList<HashPair<K,V>> chain = buckets.get(index);
    	
    	//Loop through to linked list to check if key present
    	//and set new value return old value
    	int size = chain.size();
    	for (int i=0;i<size;i++) {
    		HashPair<K,V> point = chain.get(i);
    		if (point.getKey().equals(key)) {
    			V oldValue = point.getValue();
    			point.setValue(value);
    			return oldValue;
    		}
    	}
    	//Adds the pair to chain
    	numEntries++;
    	chain.add(pair);
    	//buckets.set(index, chain);
    	
    	//if load factor goes beyond max
    	if ((1.0*numEntries)/numBuckets >= MAX_LOAD_FACTOR) {
    		rehash();
    	}
    		
    	return null;
    }
    
    
    
    
    /**
     * Get the value corresponding to key. Expected average runtime O(1)
     */
    
    public V get(K key) {
     
    	// Gives the index associated with the key
        int index = hashFunction(key);
        
        // Gives buckets of a given index to variable chain
        LinkedList<HashPair<K, V>> chain = buckets.get(index);
        int size = chain.size();
        
        
        //For loop through linked list
        for (int i=0;i<size;i++) {
        	HashPair<K,V> pair = chain.get(i);
        	if (pair.getKey().equals(key)) {
        		return pair.getValue();
        	}
        }
        
        
    	return null;
    	
    }
    
    /**
     * Remove the HashPair corresponding to key . Expected average runtime O(1) 
     */
    public V remove(K key) {
        //Gives the index associated with the key
    	int index = hashFunction(key);
    	
    	 // Gives buckets of a given index to variable chain
        LinkedList<HashPair<K, V>> chain = buckets.get(index);
        int size = chain.size();
        
        
        //For loop through linked list
        for (int i=0;i<size;i++) {
        	HashPair<K,V> pair = chain.get(i);
        	if (pair.getKey().equals(key)) {
        		V removedValue = pair.getValue();
        		chain.remove(pair);
        		numEntries--;
        		return removedValue;
        	}
        }
        
    	return null;
  
    }
    
    
    /** 
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR.
     * Made public for ease of testing.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    public void rehash() {
    	ArrayList<LinkedList<HashPair<K,V>>> old = buckets;
		buckets = new ArrayList<>();
		numBuckets = 2*numBuckets;
		numEntries = 0;
		//LinkedList<HashPair<K,V>> list = new LinkedList<HashPair<K,V>>();
		for (int i=0;i<numBuckets;i++) {
			buckets.add(new LinkedList<HashPair<K,V>>());
		}
		
		for (LinkedList<HashPair<K,V>> bucket : old) {
			for (HashPair<K,V> pair : bucket) {
				put(pair.getKey(), pair.getValue());
			}
		}
		

    }
    
    
    /**
     * Return a list of all the keys present in this hashtable.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    
    public ArrayList<K> keys() {
        ArrayList<K> list = new ArrayList<K>();
        
        for (LinkedList<HashPair<K,V>> bucket : buckets) {
        	for (HashPair<K,V> pair : bucket) {
        		list.add(pair.getKey());
        	}
        }
        
        
    	return list;
    	
    	
    	
        
    }
    
    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> values() {
    	ArrayList<V> list = new ArrayList<>();
        MyHashTable<V,K> table = new MyHashTable<V,K>(size());
        
        ArrayList<K> listKeys = keys();
        for (K key : listKeys) {
        	table.put(get(key),key);
        	
        }
        list = table.keys();
       
    	return list;
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
        ArrayList<K> sortedResults = new ArrayList<>();
        for (HashPair<K, V> entry : results) {
			V element = entry.getValue();
			K toAdd = entry.getKey();
			int i = sortedResults.size() - 1;
			V toCompare = null;
        	while (i >= 0) {
        		toCompare = results.get(sortedResults.get(i));
        		if (element.compareTo(toCompare) <= 0 )
        			break;
        		i--;
        	}
        	sortedResults.add(i+1, toAdd);
        }
        return sortedResults;
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
    	
    	ArrayList<K> listKeys = results.keys();
    	int lastIndex = listKeys.size()-1;
    	sort(listKeys, 0, lastIndex, results);
    	
     
    	return listKeys;
		
    }
    
    private static <K,V extends Comparable<V>> void sort(ArrayList<K> array, int f, int l, MyHashTable<K,V> results) {
    	if (f < l) {
    		
    		int m = (f+l)/2;
    		sort(array, f, m, results);
    		sort(array, m+1, l, results);
    		merge(array, f, m, l, results);
    	}
    }
    
    private static <K, V extends Comparable<V>> void merge(ArrayList<K> array, int f, int m, int l, MyHashTable<K,V> results) {
    	int size1 = m - f + 1;
    	int size2 = l - m;
    	
    	
    	ArrayList<K> list1 = new ArrayList<>();
    	ArrayList<K> list2 = new ArrayList<>();
    	
    	for (int i=0;i<size1;i++) {
    		list1.add(i, array.get(f + i));;
    	}
    	for (int k=0;k<size2;k++) {
    		list2.add(k, array.get(m + 1 +k));
    	}
    	int i = 0, j = 0;
    	
    	int k = f;
    	while ( i < size1 && j < size2) {
    		if (results.get(list1.get(i)).compareTo(results.get(list2.get(j))) > 0) {
    			array.set(k, list1.get(i));
    			i++;
    		} else {
    			array.set(k, list2.get(j));
    			j++;
    		}
    		k++;
    	}
    	while ( i < size1) {
    		array.set(k, list1.get(i));
			i++;
			k++;
    	}
    	while (j < size2) {
    		array.set(k, list2.get(j));
			j++;
			k++;
    	}
    }

    
    
    
    

	

	@Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }   
    
    private class MyHashIterator implements Iterator<HashPair<K,V>> {
        private int index = 0;
        private ArrayList<HashPair<K,V>> array;
    	/**
    	 * Expected average runtime is O(m) where m is the number of buckets
    	 */
        
        private MyHashIterator() {
        	array = new ArrayList<>();
        	for (LinkedList<HashPair<K,V>> pairs : buckets) {
        		for (HashPair<K,V> pair : pairs) {
        			array.add(pair);
        		}
        	}
            
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public boolean hasNext() {
            boolean has = index < array.size()-1;
        	
        	return has;
        	
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public HashPair<K,V> next() {
            HashPair<K,V> pair = array.get(index);
            index++;
        	
        	return pair;
        	
        }
        
    }
}

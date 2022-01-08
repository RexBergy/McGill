package FinalProject_Template;

import java.util.ArrayList;

public class Twitter {
	private ArrayList<Tweet> tweets;
	private ArrayList<String> stopWords;
	private MyHashTable<String,Integer> stopWordTable;
	private MyHashTable<String,Tweet> authorTweet;
	private MyHashTable<String,ArrayList<Tweet>> dateTweet;
	 
	
	// O(n+m) where n is the number of tweets, and m the number of stopWords
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
		this.authorTweet = new MyHashTable<String,Tweet>(tweets.size());
		this.dateTweet = new MyHashTable<String,ArrayList<Tweet>>(tweets.size());
		
		this.tweets = new ArrayList<Tweet>();
		for (Tweet t : tweets) {
			this.tweets.add(t);
			addTweet(t);
		}
		this.stopWords = new ArrayList<String>();
		for (String s : stopWords) {
			this.stopWords.add(s);
		}
		this.stopWordTable = new MyHashTable<String,Integer>(2);
		for (String s : stopWords) {
			s = s.toLowerCase();
			this.stopWordTable.put(s,1);
		}
		
	}
	
	
    /**
     * Add Tweet t to this Twitter
     * O(1)
     */
	public void addTweet(Tweet t) {
		// Add in author hashtable
		//Checks if already present
		 Tweet check = authorTweet.get(t.getAuthor());
		 if (check == null) {
			 authorTweet.put(t.getAuthor(), t);
		 } else {
			 if (check.compareTo(t) < 0) {
				 authorTweet.remove(t.getAuthor());
				 authorTweet.put(t.getAuthor(), t);
			 }
		 }
		 //Add in date hashtable
		 String date = t.getDateAndTime().substring(0, 10);
		 ArrayList<Tweet> array = dateTweet.get(date);
		 if (array == null) {
			 array = new ArrayList<Tweet>();
			 array.add(t);
		 } else {
			 array.add(t);
		 }
		 dateTweet.put(date, array);
	}
	

    /**
     * Search this Twitter for the latest Tweet of a given author.
     * If there are no tweets from the given author, then the 
     * method returns null. 
     * O(1)  
     */
    public Tweet latestTweetByAuthor(String author) {
        Tweet tweet = authorTweet.get(author);
    	
    	return tweet;
    	
        
    }


    /**
     * Search this Twitter for Tweets by `date' and return an 
     * ArrayList of all such Tweets. If there are no tweets on 
     * the given date, then the method returns null.
     * O(1)
     */
    public ArrayList<Tweet> tweetsByDate(String date) {
        ArrayList<Tweet> tweets = dateTweet.get(date);
    	return tweets;
    	
    }
    
	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */
    public ArrayList<String> trendingTopics() {
    	MyHashTable<String,Integer> wordFrequency;
    	ArrayList<String> wordList = new ArrayList<>();
    	wordFrequency = new MyHashTable<>(tweets.size());
    	
        for (Tweet t : tweets) {
        	String message = t.getMessage();
        	ArrayList<String> words = getWords(message);
        	ArrayList<String> newWords = new ArrayList<String>();
        	for (String word : words) {
        		word = word.toLowerCase();
        		if ((! newWords.contains(word)) && (stopWordTable.get(word) == null)) {
        			newWords.add(word);
        		}
        	}
        	for (String word : newWords) {
        		if (wordFrequency.get(word) == null) {
        			wordFrequency.put(word, 1);
        		} else {
        			wordFrequency.put(word, wordFrequency.get(word)+1);
        			
        		}
        	}
        	
        }
        wordList = MyHashTable.fastSort(wordFrequency);
    	
    	
    	
    	return wordList;  	
    }
    
    
    
    /**
     * An helper method you can use to obtain an ArrayList of words from a 
     * String, separating them based on apostrophes and space characters. 
     * All character that are not letters from the English alphabet are ignored. 
     */
    private static ArrayList<String> getWords(String msg) {
    	msg = msg.replace('\'', ' ');
    	String[] words = msg.split(" ");
    	ArrayList<String> wordsList = new ArrayList<String>(words.length);
    	for (int i=0; i<words.length; i++) {
    		String w = "";
    		for (int j=0; j< words[i].length(); j++) {
    			char c = words[i].charAt(j);
    			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
    				w += c;
    			
    		}
    		wordsList.add(w);
    	}
    	return wordsList;
    }

    

}

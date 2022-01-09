import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class US_elections {
	
	private static int maximum(int a, int b) {
		if (a>b) {
			return a;
		} else {
			return b;
		}
	}
	
	

	public static int solution(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided) {
		
		int total_delegates = 0;
		for (int i=0;i<num_states;i++) {
			total_delegates += delegates[i];
		}
		int trump_del = 0;
		int biden_del = 0;
		int[] votesTo = new int[num_states];
		int[] undDele = new int[num_states];
		int counter = 0;
		
		for (int i=0;i<num_states;i++) {
			int state_del = delegates[i];
			int state_Bid = votes_Biden[i];
			int state_Tru = votes_Trump[i];
			int state_und = votes_Undecided[i];
			
			if (state_Tru>=state_Bid+state_und) {
				trump_del += state_del;
				votesTo[i] = 0;
				undDele[i] = 0;
			} else if (state_Bid>state_Tru+state_und) {
				biden_del += state_del;
				votesTo[i] = 0;
				undDele[i] = 0;
			} else {
				votesTo[i] = (state_Bid+state_Tru+state_und)/2 -state_Bid + 1;
				undDele[i] = delegates[i];
				counter += 1;
			}
			
		}
		if (trump_del>= total_delegates/2+1) {
			return -1;
		} else {
			if (biden_del>total_delegates/2) {
				return 0;
			} else {
				
				int[] nwVotes = new int[counter];
				int[] nwDele = new int[counter];
				int k = 0;
				int j = 0;
				for (int i:votesTo) {
					if (i!=0) {
						nwVotes[j] = i;
						nwDele[j] = delegates[k];
						j++;
						
					}
					k++;
				}
				
				int max = 0;
				for (int i=0;i<counter;i++) {
					max += nwVotes[i];
				}
				int newcounter = 0;
				int capacity = total_delegates/2 -1 - biden_del;
				
				int DPmin[][] = new int[counter+1][capacity+1];			//Dynamic program to find max number of states to remove while winning
				for (int i=0;i<=counter;i++) {
					for(int p=0;p<=capacity;p++) {
						if (i==0 || p==0) {
							DPmin[i][p] = 0;
						} else if (nwDele[i-1] <= p) {
							DPmin[i][p]= maximum(DPmin[i-1][p],nwVotes[i-1]+DPmin[i-1][p-nwDele[i-1]]);
							
						} else {
							DPmin[i][p] =DPmin[i-1][p];
						}
						newcounter +=2;
					}
					newcounter +=1;
				}
				
				int min = DPmin[counter][capacity];
				
				return max-min;
				
			}
		}

		
	}

	public static void main(String[] args) {
		
		try {
			String path = args[0];
      File myFile = new File(path);
      Scanner sc = new Scanner(myFile);
      int num_states = sc.nextInt();
      int[] delegates = new int[num_states];
      int[] votes_Biden = new int[num_states];
      int[] votes_Trump = new int[num_states];
 			int[] votes_Undecided = new int[num_states];	
      for (int state = 0; state<num_states; state++){
			  delegates[state] =sc.nextInt();
				votes_Biden[state] = sc.nextInt();
				votes_Trump[state] = sc.nextInt();
				votes_Undecided[state] = sc.nextInt();
      }
      sc.close();
      int answer = solution(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
      	System.out.println(answer);
    	} catch (FileNotFoundException e) {
      	System.out.println("An error occurred.");
      	e.printStackTrace();
    	}
  	}
}
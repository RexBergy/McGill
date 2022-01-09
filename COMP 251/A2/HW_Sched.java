import java.util.*;

class Assignment implements Comparator<Assignment>{
	int number;
	int weight;
	int deadline;
	
	
	protected Assignment() {
	}
	
	protected Assignment(int number, int weight, int deadline) {
		this.number = number;
		this.weight = weight;
		this.deadline = deadline;
	}
	
	
	
	/**
	 * This method is used to sort to compare assignment objects for sorting. 
	 */
	@Override
	public int compare(Assignment a1, Assignment a2) {
		if (a1.deadline == a2.deadline) {
			if (a1.weight<a2.weight) {
				return -1;
			} else if (a1.weight>a2.weight){
				return 1;
			} else {
			return 0;
			}
		} else if (a1.deadline>a2.deadline){
			return 1;
		} else {
			return -1;
		}
		
		
	}
}

public class HW_Sched {
	ArrayList<Assignment> Assignments = new ArrayList<Assignment>();
	int m;
	int lastDeadline = 0;
	
	protected HW_Sched(int[] weights, int[] deadlines, int size) {
		for (int i=0; i<size; i++) {
			Assignment homework = new Assignment(i, weights[i], deadlines[i]);
			this.Assignments.add(homework);
			if (homework.deadline > lastDeadline) {
				lastDeadline = homework.deadline;
			}
		}
		m =size;
	}
	
	
	/**
	 * 
	 * @return Array where output[i] corresponds to the assignment 
	 * that will be done at time i.
	 */
	public int[] SelectAssignments() {
		//TODO Implement this
		//Sort assignments
		//Order will depend on how compare function is implemented
		Collections.sort(Assignments, new Assignment());
		
		// If homeworkPlan[i] has a value -1, it indicates that the 
		// i'th timeslot in the homeworkPlan is empty
		//homeworkPlan contains the homework schedule between now and the last deadline
		int[] homeworkPlan = new int[lastDeadline];
		for (int i=0; i < homeworkPlan.length; ++i) {
			homeworkPlan[i] = -1;
		}
		ArrayList<Assignment> asgmts = new ArrayList<Assignment>();
		for (Assignment a : Assignments) {
			asgmts.add(a);
		}
		int deadline = lastDeadline;
		//int j = 0;
		for (int i=lastDeadline-1;i>-1;i--) {
			if (asgmts.size()>0) {
				Assignment chosen = asgmts.get(asgmts.size()-1);
				if (chosen.deadline>= deadline) {
			
				for (Assignment a : asgmts) {
					if ((a.deadline>=deadline) && (a.weight>chosen.weight)){
						 chosen = a;
					}
				}
					homeworkPlan[i] = chosen.number;
					deadline -=1;
					asgmts.remove(chosen);
				} else {
					deadline-=1;
				}
				
			} else {
				deadline-=1;
			}
			
				//deadline += 1;
				//j++;
			//} else {
			//	while (Assignments.get(j).deadline < deadline) {
			//		j++;
			//	}
			//	homeworkPlan[i] = Assignments.get(j).number;
			//	deadline += 1;
			
				
		}
			
			

		
		return homeworkPlan;
	}
}
	


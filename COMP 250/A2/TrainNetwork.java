//PhilippeBergeron

public class TrainNetwork {
	final int swapFreq = 2;
	TrainLine[] networkLines;

    public TrainNetwork(int nLines) {
    	this.networkLines = new TrainLine[nLines];
    }
    
    public void addLines(TrainLine[] lines) {
    	this.networkLines = lines;
    }
    
    public TrainLine[] getLines() {
    	return this.networkLines;
    }
    
    public void dance() {
    	System.out.println("The tracks are moving!");
    	for (int i = 0; i<this.networkLines.length; i++) {
    		this.networkLines[i].shuffleLine();
    		//System.out.println(this.networkLines[i].toString());
    	}
    	
    }
    
    public void undance() {
    	for (int i = 0; i<this.networkLines.length; i++) {
    		this.networkLines[i].sortLine();
    		//System.out.println(this.networkLines[i].toString());
    	}
    }
    
    public int travel(String startStation, String startLine, String endStation, String endLine) {
    	
   
    		TrainLine curLine1 = null;
    		TrainStation curStation1 = null;
    		try {
    			 curLine1 = getLineByName(startLine);
    			
    		} catch (Exception LineNotFoundException){
    			System.out.println("The line was not found in the network of lines");
    		}
        	curStation1 = curLine1.findStation(startStation); //use this variable to store the current station. 
        	
        	
        	int hoursCount1 = 0;
        	System.out.println("Departing from "+startStation);
        	
        	
        	
        	TrainLine objLine = getLineByName(endLine);
        	TrainStation objStation = objLine.findStation(endStation);
        	
        	TrainStation prevStation = null;
        	
        	
        	while(curStation1 != objStation) {
        		if ((hoursCount1 % 2 == 0) && (hoursCount1 > 1)) {
        			this.dance();
        		}
        		
        		
        		TrainStation nextStation = curLine1.travelOneStation(curStation1, prevStation);
        		
        		prevStation = curStation1;
        		curStation1 = nextStation;
        		
        		hoursCount1 += 1;
        		if (! curStation1.getLine().equals(curLine1)) {
        			curLine1 = curStation1.getLine();
        		}
        	
        		
        		if(hoursCount1 == 168) {
        			System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
        			return hoursCount1;
        		}
        		
        		//prints an update on your current location in the network.
    	    	System.out.println("Traveling on line "+curLine1.getName()+":"+curLine1.toString());
    	    	System.out.println("Hour "+hoursCount1+". Current station: "+curStation1.getName()+" on line "+curLine1.getName());
    	    	System.out.println("=============================================");
    	    	
    	    	
        		}
    	    	
    	    	System.out.println("Arrived at destination after "+hoursCount1+" hours!");
    	    	return hoursCount1;
        }
        
    
    
    
    //you can extend the method header if needed to include an exception. You cannot make any other change to the header.
    public TrainLine getLineByName(String lineName) throws LineNotFoundException{
    	TrainLine lineSearch = null;
    	for (TrainLine line : networkLines) {
    		if (line.getName().equals(lineName)){
    			lineSearch = line;
    		}
    	}
    	if (lineSearch == null) {
    		throw new LineNotFoundException("No line of that name was found");
    	}
    	
    	return lineSearch;
    }
    	
    
  //prints a plan of the network for you.
    public void printPlan() {
    	System.out.println("CURRENT TRAIN NETWORK PLAN");
    	System.out.println("----------------------------");
    	for(int i=0;i<this.networkLines.length;i++) {
    		System.out.println(this.networkLines[i].getName()+":"+this.networkLines[i].toString());
    		}
    	System.out.println("----------------------------");
    }
}

//exception when searching a network for a LineName and not finding any matching Line object.
class LineNotFoundException extends RuntimeException {
	   String name;

	   public LineNotFoundException(String n) {
	      name = n;
	   }

	   public String toString() {
	      return "LineNotFoundException[" + name + "]";
	   }
	}
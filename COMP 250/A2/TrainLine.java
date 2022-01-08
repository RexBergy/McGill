import java.util.Arrays;
import java.util.Random;

//PhilippeBergeron

public class TrainLine {

	private TrainStation leftTerminus;
	private TrainStation rightTerminus;
	private String lineName;
	private boolean goingRight;
	public TrainStation[] lineMap;
	public static Random rand;

	public TrainLine(TrainStation leftTerminus, TrainStation rightTerminus, String name, boolean goingRight) {
		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		this.lineMap = this.getLineArray();
	}

	public TrainLine(TrainStation[] stationList, String name, boolean goingRight)
	/*
	 * Constructor for TrainStation input: stationList - An array of TrainStation
	 * containing the stations to be placed in the line name - Name of the line
	 * goingRight - boolean indicating the direction of travel
	 */
	{
		TrainStation leftT = stationList[0];
		TrainStation rightT = stationList[stationList.length - 1];

		stationList[0].setRight(stationList[stationList.length - 1]);
		stationList[stationList.length - 1].setLeft(stationList[0]);

		this.leftTerminus = stationList[0];
		this.rightTerminus = stationList[stationList.length - 1];
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		for (int i = 1; i < stationList.length - 1; i++) {
			this.addStation(stationList[i]);
		}

		this.lineMap = this.getLineArray();
	}

	public TrainLine(String[] stationNames, String name,
			boolean goingRight) {/*
									 * Constructor for TrainStation. input: stationNames - An array of String
									 * containing the name of the stations to be placed in the line name - Name of
									 * the line goingRight - boolean indicating the direction of travel
									 */
		TrainStation leftTerminus = new TrainStation(stationNames[0]);
		TrainStation rightTerminus = new TrainStation(stationNames[stationNames.length - 1]);

		leftTerminus.setRight(rightTerminus);
		rightTerminus.setLeft(leftTerminus);

		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;
		for (int i = 1; i < stationNames.length - 1; i++) {
			this.addStation(new TrainStation(stationNames[i]));
		}

		this.lineMap = this.getLineArray();

	}

	// adds a station at the last position before the right terminus
	public void addStation(TrainStation stationToAdd) {
		TrainStation rTer = this.rightTerminus;
		TrainStation beforeTer = rTer.getLeft();
		rTer.setLeft(stationToAdd);
		stationToAdd.setRight(rTer);
		beforeTer.setRight(stationToAdd);
		stationToAdd.setLeft(beforeTer);

		stationToAdd.setTrainLine(this);

		this.lineMap = this.getLineArray();
	}

	public String getName() {
		return this.lineName;
	}

	public int getSize() {
		int length;
		if (this.getLeftTerminus() == null) {
			length = 0;
			return length;
		}
		
		length = 1;
		TrainStation current = this.getLeftTerminus();
		while ( ! current.isRightTerminal()){
			length += 1;
			current = current.getRight();
		}
		return length;

	}

	public void reverseDirection() {
		this.goingRight = !this.goingRight;
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation travelOneStation(TrainStation current, TrainStation previous) throws StationNotFoundException {
		try {
			current = findStation(current.getName());
		} catch (Exception StationNotFoundException) {
			System.out.println("The current train station was not found on this line");
		}
		
		
		if (previous == null) {
			if (current.hasConnection) {
				return current.getTransferStation();
			} else {
				return getNext(current);
			}
			
		
		}else if (current.hasConnection) {
			
			if (previous.hasConnection){
				return getNext(current);
			} else {
				return current.getTransferStation();
			}
			
		} else {
			return getNext(current);
		}
	}

	
	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation getNext(TrainStation station) throws StationNotFoundException {
		station = findStation(station.getName());
		TrainStation nextStation = station;
		if (this.goingRight) {
			if (station.isRightTerminal()) {
				this.reverseDirection();
				nextStation = station.getLeft();
			}else {
				nextStation = station.getRight();
			}
		} else {
			if (station.isLeftTerminal()) {
				this.reverseDirection();
				nextStation = station.getRight();
			} else {
				nextStation = station.getLeft();
			}
		}
		return nextStation;
	}


	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation findStation(String name) throws StationNotFoundException {
		


		TrainStation current = this.leftTerminus;
		while (! (current.getName() == name)) {
			if (current.equals(this.rightTerminus)) {
				throw new StationNotFoundException("No staion with that name was found");
			}
			current = current.getRight();
		}
		return current;
		
	}
	private void swap(TrainStation station1, TrainStation station2) {
		
		TrainStation tmp = station1;
		
		
		station1 = station2;
		station2 = tmp;
		
	}

	public void sortLine() {
		
		

		int length = this.getSize();
		
		for (int j=0; j<length-1; j++) {
				
			for (int i=0; i<length-j-1; i++) {
				if (this.lineMap[i].getName().compareTo(this.lineMap[i+1].getName()) > 0){
					TrainStation tmp;
					tmp = this.lineMap[i];
					this.lineMap[i] = this.lineMap[i+1];
					this.lineMap[i+1] = tmp;
						
					}
				}
			
			
		}
		this.leftTerminus = this.lineMap[0];
		this.lineMap[0].setLeftTerminal();;
		
		this.rightTerminus = this.lineMap[length - 1];
		this.lineMap[length-1].setRightTerminal();
		
		for (int i=0; i<length-1; i++) {
			this.lineMap[i].setRight(this.lineMap[i+1]);
			this.lineMap[length-1-i].setLeft(this.lineMap[length-2-i]);
		}
		
		
		
		
		
		
		
		
		
	}
		
	
		

	

	public TrainStation[] getLineArray() {

		int length = this.getSize();
		TrainStation current;
		TrainStation[] arr;
		
		
		if (length < 1) {
			arr = null;
		} else {
			current = this.leftTerminus;
			arr = new TrainStation[length];
			for (int i=0; i<length; i++) {
				arr[i] = current;
				current = current.getRight();
			}
			this.lineMap = arr;
		}
		
		

		return arr;
	}

	private TrainStation[] shuffleArray(TrainStation[] array) {
		Random rand = new Random();
		
		rand.setSeed(11);

		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			TrainStation temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		this.lineMap = array;
		return array;
	}

	public void shuffleLine() {

		// you are given a shuffled array of trainStations to start with
		TrainStation[] lineArray = this.getLineArray();
		TrainStation[] shuffledArray = shuffleArray(lineArray);
		
		int length = this.getSize();
		
		
		for (int i=0; i<length; i++) {
			this.lineMap[i].setNonTerminal();
			this.lineMap[i].setLeft(null);
			this.lineMap[i].setRight(null);
		}
		
		
		
		
		this.leftTerminus = shuffledArray[0];
		this.lineMap[0].setLeftTerminal();;
		
		this.rightTerminus = shuffledArray[length - 1];
		this.lineMap[length-1].setRightTerminal();
		
		for (int i=0; i<length-1; i++) {
			this.lineMap[i].setRight(shuffledArray[i+1]);
			this.lineMap[length-1-i].setLeft(shuffledArray[length-2-i]);
		}
		
		//this.lineMap[0] = shuffledArray[0];
		//this.lineMap[length-1] = shuffledArray[length-1];
		//TrainStation curr = shuffledArray[0];
		
		//for (int i=0; i<shuffledArray.length; i++) {
			//TrainStation curr = shuffledArray[i];
			
			//if (! curr.equals(rightTerminus)) {
			//	curr.setRight(shuffledArray[i+1]);
			//	curr.setNonTerminal();
			//}
			//if (curr.getName() == rightTerminus.getName()) {
			//	curr.setRightTerminal();
			//	curr.setRight(null);
			//}
			
			//if (! curr.equals(leftTerminus)) {
			//	curr.setLeft(shuffledArray[i-1]);
			//	curr.setNonTerminal();
			//}
			//if (curr.getName() == leftTerminus.getName()) {
			//	curr.setLeftTerminal();
			//	curr.setLeft(null);
			//}
			
		//}
		//this.lineMap = shuffledArray;
		
		
		//this.lineMap[0].setRight(shuffledArray[1]);
		//this.lineMap[length-1].setLeft(shuffledArray[length-2]);
		//for (int i=0; i<length-1; i++) {
			//this.lineMap[i].setRight(shuffledArray[i+1]);
			//this.lineMap[i].setLeft(shuffledArray[i-1]);
		//}
		
		//for (int i=1; i<length ; i++) {
		//	this.lineMap[i].setLeft(shuffledArray[i-1]);
		//}

		/*this.lineMap[0].setLeftTerminal();
		this.lineMap[0] = shuffledArray[0];
		this.lineMap[0].setRight(shuffledArray[1]);
		
		this.lineMap[this.getSize()-1] = shuffledArray[this.getSize()-1];
		this.lineMap[this.getSize()-1].setRightTerminal();
		this.lineMap[this.getSize()-1].setLeft(shuffledArray[this.getSize()-2]);
		
		for (int i = 1; i<this.getSize()-1; i++) {
			this.lineMap[i].setRight(shuffledArray[i+1]);
			this.lineMap[i].setLeft(shuffledArray[i-1]);
		}*/

	}

	public String toString() {
		TrainStation[] lineArr = this.getLineArray();
		String[] nameArr = new String[lineArr.length];
		for (int i = 0; i < lineArr.length; i++) {
			nameArr[i] = lineArr[i].getName();
		}
		return Arrays.deepToString(nameArr);
	}

	public boolean equals(TrainLine line2) {

		// check for equality of each station
		TrainStation current = this.leftTerminus;
		TrainStation curr2 = line2.leftTerminus;

		try {
			while (current != null) {
				if (!current.equals(curr2))
					return false;
				else {
					current = current.getRight();
					curr2 = curr2.getRight();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public TrainStation getLeftTerminus() {
		return this.leftTerminus;
	}

	public TrainStation getRightTerminus() {
		return this.rightTerminus;
	}
}

//Exception for when searching a line for a station and not finding any station of the right name.
class StationNotFoundException extends RuntimeException {
	String name;

	public StationNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "StationNotFoundException[" + name + "]";
	}
}


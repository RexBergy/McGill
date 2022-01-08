package Asg1;

public class Airport {
	
	private int x_coord;
	private int y_coord;
	private int fees;
	
	public Airport(int x_coord, int y_coord, int fees) {
		this.x_coord = x_coord;
		this.y_coord = y_coord;
		this.fees = fees;
	}
	
	public int getFees() {
		return fees;
	}
	
	public static  int getDistance(Airport air1, Airport air2) {
		double distance;
		distance =  Math.sqrt(Math.pow((air1.x_coord-air2.x_coord), 2) + Math.pow((air1.y_coord-air2.y_coord), 2));
		distance = Math.ceil(distance);
		return (int) distance;
				
		
		
	}
	


}

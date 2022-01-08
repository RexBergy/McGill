package Asg1;

public class BnBReservation extends HotelReservation{
	
	public BnBReservation(String name, Hotel hotel, String room_type, int num_nights) {
		super(name, hotel, room_type, num_nights);
	}
	
	public int getCost() {
		int total_cost = super.getNumOfNights()*1000 + super.getCost();
		return total_cost;
	}



}

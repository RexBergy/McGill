package Asg1;

public class HotelReservation extends Reservation {
	private Hotel hotel;
	private String type_room;
	private int num_nights;
	private int price_night;
	
	public HotelReservation(String name, Hotel hotel, String type_room, int num_nights) {
		super(name);
		this.hotel = hotel;
		this.type_room = type_room;
		
		this.price_night = hotel.reserveRoom(type_room);
		
		this.num_nights = num_nights;
	}
	
	public int getNumOfNights() {
		return num_nights;
	}
	
	public int getCost() {
		return price_night*num_nights;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof HotelReservation) {
			HotelReservation ob = (HotelReservation) obj;
			if (this.type_room == ob.type_room && this.hotel == ob.hotel &&
			this.num_nights == ob.num_nights && ob.reservationName() == this.reservationName()
				&& this.price_night == ob.price_night) {
				return true;
				} else {
				return false;
				}
			}  else {
			return false;
			}
		}

}

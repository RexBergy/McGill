package Asg1;

public class FlightReservation extends Reservation{
	private Airport dep;
	private Airport arr;
	
	public FlightReservation(String name, Airport dep, Airport arr) {
		super(name);
		
		if (dep.equals(arr)) {
			throw new IllegalArgumentException("Must be two different airports");
		} else {
			this.dep = dep;
			this.arr = arr;
		}

	}
	
	public int getCost() {
		double air_fees = dep.getFees() + arr.getFees();
		
		double distance = Airport.getDistance(dep, arr);
		double gallons_fuel = distance/167.52;
		double price_fuel = gallons_fuel*124.00;
		
		double price_cents = air_fees + price_fuel + 5375.00;
		price_cents = Math.ceil(price_cents);
		
		return (int) price_cents;
	}
	
	
	public boolean equals(Object obj) {
		if (obj instanceof FlightReservation) {
			FlightReservation ob = (FlightReservation) obj;
			if (ob.dep == this.dep && ob.arr == this.arr && 
					ob.reservationName() == this.reservationName()) {
				return true;
				} else {
					return false;
					}
			} else {
				return false;
				}
		}
}
	



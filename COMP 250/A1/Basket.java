package Asg1;

public class Basket {
	private Reservation[] reservations;
	
	public Basket() {
		this.reservations = new Reservation[0];
	}
	
	public Reservation[] getProducts() {
		Reservation[] products = reservations;
		return products;
		
	}
	
	public int add(Reservation rserv) {
		int num_rserv = reservations.length;
		num_rserv += 1;
		Reservation[] new_rserv = new Reservation[num_rserv];
		for (int i=0; i<reservations.length; i++) {
			new_rserv[i] = reservations[i];
		}
		new_rserv[num_rserv-1] = rserv;
		reservations = new_rserv;
		return num_rserv;
	}
	
	public boolean remove(Reservation rserv) {
		int num_rserv = reservations.length;
		num_rserv -= 1;
		Reservation[] new_rserv = new Reservation[num_rserv];
		boolean erased = false;
		for (int i=0; i<num_rserv; i++) {
			if (reservations[i].equals(rserv)) {
				new_rserv[i] = reservations[i+1];
				erased = true;
			} else {
				new_rserv[i] = reservations[i];
			}
		}
			
		reservations = new_rserv;
		return erased;
	}
	
	public void clear() {
		reservations = new Reservation[0];
	}
	
	public int getNumOfReservations() {
		return reservations.length;
	}
	
	public int getTotalCost() {
		int cost = 0;
		for (int i=0; i<reservations.length; i++) {
			cost += reservations[i].getCost();
			}
		return cost;
	}


}

package Asg1;

public class Customer {
	
	private String name;
	private int balance;
	private Basket basket;
	
	public Customer(String name, int balance) {
		this.name = name;
		this.balance = balance;
		this.basket = new Basket();
		
	}
	
	public String getName() {
		return name;
	}
	
	public int getBalance() {
		return balance;
		
	}
	
	public Basket getBasket() {
		return basket;
	}
	public int addFunds(int amount) {
		if(amount < 0) {
			throw new IllegalArgumentException("Cannot add negative amounts");
		} else {
			balance += amount;
			return balance;
		}
	}
	
	public int addToBasket(Reservation rserv) {
		if (rserv.reservationName() == name) {
			basket.add(rserv);
			return basket.getNumOfReservations();
		} else {
			throw new IllegalArgumentException("The costuumer's name does not match the rservation's name");
		}
	}
	
	public int addToBasket(Hotel hotel, String type, int nights, boolean breakfast) {
		if (breakfast == true) {
			basket.add(new BnBReservation(this.getName(), hotel, type, nights));
		} else {
			basket.add(new HotelReservation(this.getName(), hotel, type, nights));
		}
		return basket.getNumOfReservations();
		
	}
	
	public int addToBasket(Airport air1, Airport air2) {
		basket.add(new FlightReservation(this.getName(), air1, air2));
		return basket.getNumOfReservations();
	}
	
	public boolean removeFromBasket(Reservation rserv) {
		return basket.remove(rserv);
	}
	
	public int checkOut() {
		if (balance < basket.getTotalCost()) {
			throw new IllegalArgumentException("Not enough funds in your balance");
			
		} else {
			balance -= basket.getTotalCost();
			basket.clear();
			return balance;
		}
	}
	


}

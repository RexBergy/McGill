package Asg1;

public class Room {
	
	private String type;
	private int price;
	private boolean availability;
	
	public Room(String type) {
		
		if (type.equalsIgnoreCase("double")){
			this.type = type;
			this.price = 9000;
			this.availability = true;
		} else if (type.equalsIgnoreCase("queen")) {
			this.type = type;
			this.price = 11000;
			this.availability = true;
		} else if (type.equalsIgnoreCase("king")) {
			this.type = type;
			this.price = 15000;
			this.availability = true;
		} else {
			throw new IllegalArgumentException("No such type of room can be created");
				
			}


	}
	
	public Room(Room room) {
		this.type = room.type;
		this.price = room.price;
		this.availability = room.availability;
	}
		
	
	
	public String getType() {
		return type;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void changeAvailability() {
		availability = ! availability;
	}
	
	public static Room findAvailableRoom(Room[] roomArr, String type) {
		for (Room rm : roomArr) {
			if ((rm.getType().equalsIgnoreCase(type)) && (rm.availability == true)) {
				return rm;
			}
		}
		return null;
	}
	
	private static Room findUnavailableRoom(Room[] roomArr, String type) {
		for (Room rm : roomArr) {
			if ((rm.getType().equalsIgnoreCase(type)) && (rm.availability == false)) {
				return rm;
			}
		}
		return null;
	}
	
	public static boolean makeRoomAvailable(Room[] roomArr, String type) {
		Room search = findUnavailableRoom(roomArr, type);
		if (search == null){
			return false;
		} else {
			search.changeAvailability();
			return true;
		}
	}



}

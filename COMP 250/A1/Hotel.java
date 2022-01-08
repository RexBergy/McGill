package Asg1;

public class Hotel {
	
	private String name;
	private Room[] rooms;
	
	public Hotel(String name, Room[] rooms) {
		this.name = name;
		this.rooms = new Room[rooms.length];
		for (int i=0; i<rooms.length; i++) {
			this.rooms[i] = new Room(rooms[i].getType());
			}
		}
		
		
	
	
	public int reserveRoom(String type) {
		;
		Room search  = Room.findAvailableRoom(this.rooms, type);
		if (search == null) {
			throw new IllegalArgumentException("No available rooms of that type were found");
		} else {
			search.changeAvailability();
			return search.getPrice();
		}
		
	}
	
	public boolean cancelRoom(String type) {
		if (Room.makeRoomAvailable(this.rooms, type) == true) {
			return true;
		} else {
			return false;
		}
	}
	


}

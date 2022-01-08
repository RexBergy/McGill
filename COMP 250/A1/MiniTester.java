package Asg1;
import java.lang.reflect.Field;

import Airport;
import Basket;
import BnBReservation;
import Customer;
import FlightReservation;
import Hotel;
import HotelReservation;
import Reservation;
import Room;

public class MiniTester 
{ 
 int scale = 2;
 String packageName;

 private String gradeString(int score, int maxScore, String comment)
 {
  return comment + "Score: " + Integer.toString(score * scale) + "/" + Integer.toString(maxScore * scale);
 }

 private void write(String string)
 {
  System.out.print(string + "\n");
 }

 public MiniTester()
 {
  Package p = MiniTester.class.getPackage();
  packageName = p != null ? p.getName() + "." : "";
 }

 private boolean getAvailability(Room r) throws Exception
 { 
  boolean availability = false;

  try {
   Field []fields = Room.class.getDeclaredFields();
   Field fAvailability = null;

   for (Field f : fields) {
    if (f.getType().equals(boolean.class)) {
     fAvailability = f;
    }
   }

   if (fAvailability == null)
    throw new Exception("Boolean field availability not found in class Room");

   fAvailability.setAccessible(true);
   availability = (boolean)fAvailability.get(r);
  }
  catch (Exception e) {
   throw new Exception("Boolean field availability not found in class Room");
  }

  return availability; 
 }

 private Room[] getRooms(Hotel h) throws Exception
 {
  Room[] rooms = null;
  try {
   Field []fields = Hotel.class.getDeclaredFields();
   Field fRooms = null;

   for (Field f : fields) {
    if (f.getType().getName().equals("[L" + packageName + "Room;")) {
     fRooms = f;
    }
   }

   if (fRooms == null)
    throw new Exception("Array field Room[] not found in class Hotel");

   fRooms.setAccessible(true);
   rooms = (Room[])fRooms.get(h);
  }
  catch (Exception e) {
   throw new Exception("Array field Room[] not found in class Hotel");
  }

  return rooms;
 }

 private Room findRoomOfType(Room[] rooms, String type) throws Exception {
  for (int i=0; i<rooms.length; i++) {
   if(rooms[i].getType().equalsIgnoreCase(type))
    return rooms[i];
  }

  throw new Exception("Room of type " + type + " not found in the array of Rooms");
 }

 //test cases
 private int testRoom_0(int testIdx)
 {
  String comment = "[" + testIdx + "]: Test whether Room type and price are set correctly.\n";
  int maxScore = 3;
  int grade = 0;

  try {
   Room r1 = new Room("double");
   Room r2 = new Room("king");
   Room r3 = new Room("queen");

   if (r1.getPrice() == 9000 && r1.getType().equalsIgnoreCase("double"))
    grade += 1;
   else {
    comment = comment + "Error: Price or Type for room type - double - is incorrect.\n";
   }

   if (r2.getPrice() == 15000 && r2.getType().equalsIgnoreCase("king"))
    grade += 1;
   else {
    comment = comment + "Error: Price or Type for room type - king - is incorrect.\n";
   }

   if (r3.getPrice() == 11000 && r3.getType().equalsIgnoreCase("queen"))
    grade += 1;
   else {
    comment = comment + "Error: Price or Type for room type - queen - is incorrect.\n";
   }

   write(gradeString(grade, maxScore, comment));
  }
  catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testRoom_2(int testIdx) {
  String comment = "[" + testIdx + "]: Test whether findAvailableRooms returns the FIRST available.\n";
  int maxScore = 2;
  int grade = 0;

  try {

   //multiple of same type
   Room[] rooms = new Room[4];
   Room r1 = new Room("double");
   Room r2 = new Room("queen");
   Room r3 = new Room("king");
   Room r4 = new Room("queen");
   rooms[0] = r1;
   rooms[1] = r2;
   rooms[2] = r3;
   rooms[3] = r4;
   Room found = Room.findAvailableRoom(rooms, "queen");
   if (r2 == found) grade += 1;
   else {
    comment = comment + "Error: findAvailableRoom doesn't find the first available room if multiples of that type exist.\n";

   }

   //no matching room or none available
   Room r5 = new Room("double");
   Room r6 = new Room("queen");
   Room[] rooms2 = new Room[2];
   rooms2[0] = r5;
   rooms2[1] = r6;
   found = Room.findAvailableRoom(rooms2, "king");
   if (found == null) grade += 1;
   else {
    comment = comment + "Error: findAvailableRoom doesn't return null if none exist.\n";

   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testRoom_3(int testIdx) {
  String comment = "[" + testIdx + "]: Test whether makeRoomAvailable changes the FIRST unavailable to available.\n";
  int maxScore = 1;
  int grade = 0;

  try {
   Room r1 = new Room("double");
   Room r2 = new Room("queen");
   Room r3 = new Room("queen");
   Room[] rooms = new Room[3];
   rooms[0] = r1;
   rooms[1] = r2;
   rooms[2] = r3;
   boolean res = Room.makeRoomAvailable(rooms, "queen");

   //true case
   r3.changeAvailability();
   res = Room.makeRoomAvailable(rooms, "queen");
   if (res == true && getAvailability(r3) == true) grade += 1;
   else {
    comment = comment + "Error: makeRoomAvailable didn't change the right room's availablility or it didn't return true.\n";
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testAirport_1(int testIdx) {
  String comment = "[" + testIdx + "]: Test whether getDistance() is calculated correctly.\n";
  int maxScore = 4;
  int grade = 0;

  try {
   Airport a1 = new Airport(4, 3, 100);
   Airport a3 = new Airport(7, 8, 300);

   //different coordinates
   int distance2 = Airport.getDistance(a1, a3);

   if (distance2 == 6) grade += 4;
   else {
    comment = comment + "Error: getDistance doesn't return the right value when Airports have different coordinates.\n";
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testHotel_1(int testIdx) {
  String comment = "[" + testIdx + "]: Test whether Rooms[] is a deep copy and reserveRoom / cancelRoom works.\n";
  int maxScore = 6;
  int grade = 0;

  try {
   Room[] rooms = new Room[2];
   Room r1 = new Room("double");
   Room r2 = new Room("queen");
   rooms[0] = r1;
   rooms[1] = r2;

   Hotel h1 = new Hotel("Test Hotel", rooms);
   int price = h1.reserveRoom("double"); // first reservation is okay

   Room studentRoom = findRoomOfType(getRooms(h1), "double");

   if (getAvailability(r1) == true && price == 9000 && getAvailability(studentRoom) == false) grade += 3; //shouldn't have updated the original room since a deep copy
   else {
    comment = comment + "Error: Hotel constructor doesn't make an array of Rooms that is a deep copy or reserveRoom doesn't work.\n";
    write(gradeString(grade, maxScore, comment));
    return 0;
   }

   boolean res1 = h1.cancelRoom("double");
   studentRoom = findRoomOfType(getRooms(h1), "double");
   if (res1 == true && getAvailability(studentRoom) == true) grade += 3;
   else {
    comment = comment + "Error: cancelRoom doesn't change the availability of Room.\n";
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testFlightReservation_0(int testIdx) {
  String comment = "[" + testIdx + "]: Test whether getCost is calculated correctly.\n";
  int maxScore = 4;
  int grade = 0;

  try {
   Airport a1 = new Airport(4, 30000, 20000);
   Airport a2 = new Airport(7, 80000, 30000);
   FlightReservation f1 = new FlightReservation("myFlight", a1, a2);
   int cost = f1.getCost(); // should be 92387 by hard coding the calculation
   if (cost == 92387) grade += 4;
   else {
    comment = comment + "Error: getCost in FlightReservation isn't calculated correctly.\n";
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testHotelReservation_0(int testIdx) {
  String comment = "[" + testIdx + "]: Test whether room is reserved when a HotelReservation is made.\n";
  int maxScore = 3;
  int grade = 0;

  try {
   Room room1 = new Room("double");
   Room room2 = new Room("double");
   Room room3 = new Room("queen");
   Room[] rooms = {room1, room2, room3};
   Hotel h1 = new Hotel("myHotel", rooms);
   HotelReservation reserve1 = new HotelReservation("Jack", h1, "double", 3);
   HotelReservation reserve2 = new HotelReservation("Jill", h1, "double", 3);

   // No 'double' rooms are available at this point anymore
   try {
    h1.reserveRoom("double");
   } catch (IllegalArgumentException e) {
    grade += 3;
   }
   if (grade < 1) 
    comment = comment + "Error: making a HotelReservation actually should reserve the room (availability changes).\n";

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testHotelReservation_2(int testIdx) {
  String comment = "[" + testIdx + "]: Test whether equals is implemented correctly.\n";
  int maxScore = 4;
  int grade = 0;

  try {
   Room room1 = new Room("double");
   Room room2 = new Room("double");
   Room room3 = new Room("queen");
   Room[] rooms = {room1, room2, room3};
   Hotel h1 = new Hotel("myHotel", rooms);
   HotelReservation reserve1 = new HotelReservation("myReservation", h1, "double", 3);
   HotelReservation reserve2 = new HotelReservation("myReservation", h1, "double", 3);

   //true case
   boolean res = reserve1.equals(reserve2);
   if (res == true) grade += 2;
   else {
    comment = comment + "Error: equals in HotelReservation returns false when equal.\n";
   }

   //false case - different HotelReservation
   HotelReservation reserve3 = new HotelReservation("myNewReservation", h1, "queen", 3);
   res = reserve1.equals(reserve3);
   if (res == false) grade += 2;
   else {
    comment = comment + "Error: equals in HotelReservation returns true when both FlightReservations but not equal.\n";
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testHotelBnB_0(int testIdx) {
  String comment = "[" + testIdx + "]: Test that getCost is set correctly.\n";
  int maxScore = 3;
  int grade = 0;

  try {
   Room room1 = new Room("double");
   Room room2 = new Room("double");
   Room room3 = new Room("queen");
   Room[] rooms = {room1, room2, room3};
   Hotel h1 = new Hotel("myHotel", rooms);
   BnBReservation r1 = new BnBReservation("myReservation", h1, "double", 3);
   int cost = r1.getCost(); //should be 30000 by hard coding the calculation
   if (cost == 30000) grade += 3;
   else {
    comment = comment + "Error: getCost in HotelReservation isn't calculated correctly.\n";
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testBasket_0(int testIdx) {
  String comment = "[" + testIdx + "]: Test that add method adds to the end of the list.\n";
  int maxScore = 3;
  int grade = 0;

  try {
   Basket b1 = new Basket();
   Airport a1 = new Airport(7, 80000, 30000);
   Airport a2 = new Airport(8, 50000, 10000);
   FlightReservation r1 = new FlightReservation("myReservation", a1, a2);
   FlightReservation r2 = new FlightReservation("secondReservation", a2, a1);

   b1.add(r1);
   Reservation[] myReservations = b1.getProducts();
   if (r1.equals(myReservations[0])) grade += 1;
   else {
    comment = comment + "Error: add in Basket doesn't add a Reservation if the basket is empty at first.\n";
   }
   b1.add(r2);
   myReservations = b1.getProducts();
   if (r2.equals(myReservations[1]) && r1.equals(myReservations[0])) grade += 2;
   else {
    comment = comment + "Error: add in Basket doesn't add to the end of the list.\n";
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testBasket_2(int testIdx) {
  String comment = "[" + testIdx + "]: Test that remove method can remove and shift list if such a reservation exists.\n";
  int maxScore = 6;
  int grade = 0;

  try {

   Basket b1 = new Basket();
   Airport a1 = new Airport(7, 80000, 30000);
   Airport a2 = new Airport(8, 50000, 10000);
   Airport a3 = new Airport(1400, 70000, 10000);
   FlightReservation r1 = new FlightReservation("myReservation", a1, a2);
   FlightReservation r2 = new FlightReservation("secondReservation", a1, a3);
   FlightReservation r3 = new FlightReservation("thirdReservation", a2, a3);
   b1.add(r1);
   b1.add(r2);
   b1.add(r3);

   boolean res = b1.remove(r2);
   Reservation[] myProducts = b1.getProducts();
   if(res == true && myProducts.length == 2 && myProducts[0].equals(r1) && myProducts[1].equals(r3)) {
    grade += 6;
   } else {
    comment = comment + "Error: Basket.remove() doesn't shift the products down.\n";
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testCustomer_1(int testIdx) {
  String comment = "[" + testIdx + "]: Test that addFunds is correct.\n";
  int maxScore = 2;
  int grade = 0;

  try {
   Customer c1 = new Customer("John", 200000);
   c1.addFunds(10000);
   if (c1.getBalance() == 210000) grade += 2;
   else {
    comment = comment + "Error: addFunds in Customer doesn't add to the balance correctly.\n";
   }
   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testCustomer_2(int testIdx) {
  String comment = "[" + testIdx + "]: Test that addToBasket works.\n";
  int maxScore = 2;
  int grade = 0;

  try {
   //Reservation r
   Customer c1 = new Customer("John", 200000);

   Airport a1 = new Airport(7, 80000, 30000);
   Airport a2 = new Airport(8, 50000, 10000);
   FlightReservation r1 = new FlightReservation("John", a1, a2);

   c1.addToBasket(r1);

   Room room1 = new Room("double");
   Room room2 = new Room("queen");
   Room room3 = new Room("king");
   Room[] rooms = {room1, room2, room3};
   Hotel h1 = new Hotel("myHotel", rooms);
   HotelReservation r2 = new HotelReservation("John", h1, "double", 3);

   c1.addToBasket(r2);

   if (c1.getBasket().getNumOfReservations() == 2) grade += 2;
   else {
    comment = comment + "Error: addtoBasket(Reservation r) is not implemented correctly.\n";
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }

 public int testCustomer_4(int testIdx) {
  String comment = "[" + testIdx + "]: Test that checkOut works (if enough funds, and if not enough funds).\n";
  int maxScore = 2;
  int grade = 0;

  try {
   //enough funds
   Customer c1 = new Customer("John", 20000000);

   Airport a1 = new Airport(7, 80000, 30000);
   Airport a2 = new Airport(8, 50000, 10000);
   FlightReservation r1 = new FlightReservation("John", a1, a2);
   FlightReservation r2 = new FlightReservation("John", a2, a1);

   c1.addToBasket(r1);
   c1.addToBasket(r2);
   //total cost of basket is 135166
   int remaining = c1.checkOut();  //remaining should = 20000000 - 135166 = 19864834
   if (remaining == 19864834) grade += 1;
   else {
    comment = comment + "Error: when checking out the remaining balance returned is incorrect.\n";
   }

   if (c1.getBasket().getNumOfReservations() == 0 && c1.getBasket().getProducts().length == 0) {
    grade += 1;
   } else {
    comment = comment + "Error: when checking out the basket isn't cleared.\n"; 
   }

   write(gradeString(grade, maxScore, comment));

  } catch (Exception e) {
   comment = comment + "Exception Found: " + e.toString() + "\n";
   e.printStackTrace();
   write(gradeString(0, maxScore, comment));
  }

  return grade;
 }
 //----------------------------------------------------------------------------------------------------------------------------------------

 public static void main(String[] args) 
 {
  MiniTester m = new MiniTester();
  int total = 0;
  //ROOM 6
  total += m.testRoom_0(0);
  total += m.testRoom_2(1);
  total += m.testRoom_3(2);
  //AIRPORT 4
  total += m.testAirport_1(3);
  //HOTEL 6
  total += m.testHotel_1(4);
  //FLIGHT RESERVATION 4
  total += m.testFlightReservation_0(5);
  //HOTEL RESERVATION 7
  total += m.testHotelReservation_0(6);
  total += m.testHotelReservation_2(7);
  //HOTEL RESERVATION WITH BREAKFAST 3
  total += m.testHotelBnB_0(8);
  //BASKET 9
  total += m.testBasket_0(9);
  total += m.testBasket_2(10);
  //CUSTOMER 6 
  total += m.testCustomer_1(11);
  total += m.testCustomer_2(12);
  total += m.testCustomer_4(13);

  m.write(m.gradeString(total+5, 50, "Your Final Tester "));  //extra 5 for the syntax tester
 }
}



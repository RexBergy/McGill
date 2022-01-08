package Asg1;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SyntaxTester {
 
 private static boolean classExists(String className) 
 {
        try {
            Class.forName( className );
            return true;
        }

        catch( Exception e ){ return false; }
    }
 
 private static void testFnNames(String className, ArrayList<String> correctMethods, ArrayList<String> correctConstructors) throws Exception {
        if (classExists(className)) // Proceed only if the class exists
        {
            Class<?> theClass = null;
            try {
                theClass = Class.forName(className); // get the class e.g. Hotel or Airport

            }catch (Exception e)
            {
             throw new Exception("Could not find class - " + className);
            }

            Method[] studentMethods = theClass.getDeclaredMethods(); // get the public methods
            @SuppressWarnings("rawtypes")
			Constructor[] studentConstr = theClass.getConstructors(); // get the constructor methods

            ArrayList<String> s_studentMethods = new ArrayList< String >();
            ArrayList< String > s_studentConstr = new ArrayList< String >();

            for( Method method : studentMethods ) s_studentMethods.add ( method.toString() );
            for( Constructor<?> constr : studentConstr ) s_studentConstr.add ( constr.toString() );
            
            //System.out.println(s_studentMethods);
            //System.out.println(s_studentConstr);
            
            evaluate(correctConstructors, s_studentConstr, true, className);
            evaluate(correctMethods, s_studentMethods, false, className);

        }
        else
            throw new Exception("Class " + className + " does not exist.");
    }
 
 private static void evaluate(ArrayList<String> correct, ArrayList<String> student, boolean constr, String className) throws Exception {
        for (String correctString : correct) //we only care about testable methods
        {
            if (!student.contains(correctString)) //signature cannot be found
            { 
             String errStr;
                if (constr) {
                    errStr = "Error with constructor: " + correctString + " in class: " + className + ". Constructor cannot be found or has wrong signature.";
                } else {
                    errStr = "Error with method: " + correctString + " in class: " + className + ". Method cannot be found or has wrong signature.";
                }

                throw new Exception(errStr);
            }
        }
    }
 
 private static void checkField(String className, String fieldType, int quantity, boolean strict) throws Exception
 {
  Class<?> _class = null;
  
  try {
      // get the class
      _class = Class.forName(className);
           
        }catch (Exception e)
        {
            throw new Exception("Could not find class - " + className);
        }
  
  // get all the fields in the class
  Field []fields = _class.getDeclaredFields();
  
  int foundQty = quantity;
  for (Field f : fields) {
   //System.out.println(f.getType().getName());
   if (f.getType().getName().equals(fieldType)) 
       foundQty--;
  }
  // Must meet the minimum required number of fields
  if ((strict && foundQty != 0) || (!strict && foundQty > 0))
      throw new Exception("Class " + className + " does not contain the required number of field(s) of type - " + fieldType);
 }
 
 public static void main(String[] args)
 { 
  
  try 
  { 
   SyntaxTester s = new SyntaxTester();
   Package p = s.getClass().getPackage();
   String packageName = p != null ? p.getName() + "." : "";
      
   // check if all classes exist
   String []classNames = {"Airport", "Basket", "Customer", "Hotel", "Room",
     "Reservation", "FlightReservation", "HotelReservation", 
     "BnBReservation" };
   
   for (String className : classNames)
    if (!classExists(packageName + className))
     throw new Exception("Please implement class - " + className);
   
   // check if all functions and constructors exist
   // Class - Room
   ArrayList<String> correctFnNamesRoom = new ArrayList<String>( Arrays.asList(
                 "public int " + packageName + "Room.getPrice()",
                 "public java.lang.String " + packageName + "Room.getType()",
                 "public void " + packageName + "Room.changeAvailability()",
                 "public static boolean " + packageName + "Room.makeRoomAvailable(" + packageName + "Room[],java.lang.String)",
                 "public static " + packageName + "Room "  + packageName + "Room.findAvailableRoom(" + packageName + "Room[],java.lang.String)"
                 ) );
         ArrayList<String> correctConstNamesRoom = new ArrayList<String>(Arrays.asList( "public " + packageName + "Room(java.lang.String)", "public " + packageName + "Room(" + packageName + "Room)" ));
         testFnNames(packageName + "Room", correctFnNamesRoom, correctConstNamesRoom);

         //Class - Hotel
         ArrayList<String> correctFnNamesHotel = new ArrayList<String>( Arrays.asList(
                 "public int " + packageName + "Hotel.reserveRoom(java.lang.String)",
                 "public boolean " + packageName + "Hotel.cancelRoom(java.lang.String)"
                 ) );
         ArrayList<String> correctConstNamesHotel = new ArrayList<String>(Arrays.asList( "public " + packageName + "Hotel(java.lang.String," + packageName + "Room[])"));
         testFnNames(packageName + "Hotel", correctFnNamesHotel, correctConstNamesHotel);
         
         //Class - Airport
         ArrayList<String> correctFnNamesAirport = new ArrayList<String>( Arrays.asList(
                 "public int " + packageName + "Airport.getFees()",
                 "public static int " + packageName + "Airport.getDistance(" + packageName + "Airport," + packageName + "Airport)"
                 ) );
         ArrayList<String> correctConstNamesAirport = new ArrayList<String>(Arrays.asList( "public " + packageName + "Airport(int,int,int)"));
         testFnNames(packageName + "Airport", correctFnNamesAirport, correctConstNamesAirport);
         
         //Class - Reservation
         ArrayList<String> correctFnNamesReservation = new ArrayList<String>( Arrays.asList(
                 "public final java.lang.String " + packageName + "Reservation.reservationName()",
                 "public abstract int " + packageName + "Reservation.getCost()",
                 "public abstract boolean " + packageName + "Reservation.equals(java.lang.Object)"
                 ) );
         ArrayList<String> correctConstNamesReservation = new ArrayList<String>(Arrays.asList( "public " + packageName + "Reservation(java.lang.String)"));
         testFnNames(packageName + "Reservation", correctFnNamesReservation, correctConstNamesReservation);
         
         //Class - FlightReservation
         ArrayList<String> correctFnNamesFlightReservation = new ArrayList<String>( Arrays.asList(
                 "public int " + packageName + "FlightReservation.getCost()",
                 "public boolean " + packageName + "FlightReservation.equals(java.lang.Object)"
                 ) );
         ArrayList<String> correctConstNamesFlightReservation = new ArrayList<String>(Arrays.asList( "public " + packageName + "FlightReservation(java.lang.String,"
           + packageName +"Airport," + packageName + "Airport)"));
         testFnNames(packageName + "FlightReservation", correctFnNamesFlightReservation, correctConstNamesFlightReservation);
         
         //Class - HotelReservation
         ArrayList<String> correctFnNamesHotelReservation = new ArrayList<String>( Arrays.asList(
                 "public int " + packageName + "HotelReservation.getNumOfNights()",
           "public int " + packageName + "HotelReservation.getCost()",
                 "public boolean " + packageName + "HotelReservation.equals(java.lang.Object)"
                 ) );
         ArrayList<String> correctConstNamesHotelReservation = new ArrayList<String>(Arrays.asList( "public " + packageName + "HotelReservation(java.lang.String," + packageName + "Hotel,java.lang.String,int)"));
         testFnNames(packageName + "HotelReservation", correctFnNamesHotelReservation, correctConstNamesHotelReservation);
         
         //Class - BnBReservation
         ArrayList<String> correctFnNamesBnBReservation = new ArrayList<String>( Arrays.asList(
           "public int " + packageName + "BnBReservation.getCost()"
                 ) );
         ArrayList<String> correctConstNamesBnBReservation = new ArrayList<String>(Arrays.asList( "public " + packageName + "BnBReservation(java.lang.String," + packageName + "Hotel,java.lang.String,int)"));
         testFnNames(packageName + "BnBReservation", correctFnNamesBnBReservation, correctConstNamesBnBReservation);
         
         //Class - Basket
         ArrayList<String> correctFnNamesBasket = new ArrayList<String>( Arrays.asList(
           "public " + packageName + "Reservation[] " + packageName + "Basket.getProducts()",
           "public int " + packageName + "Basket.add(" + packageName + "Reservation)",
           "public boolean " + packageName + "Basket.remove(" + packageName + "Reservation)",
           "public void " + packageName + "Basket.clear()",
           "public int " + packageName + "Basket.getNumOfReservations()",
           "public int " + packageName + "Basket.getTotalCost()"
                 ) );
         ArrayList<String> correctConstNamesBasket = new ArrayList<String>(Arrays.asList( "public " + packageName + "Basket()"));
         testFnNames(packageName + "Basket", correctFnNamesBasket, correctConstNamesBasket);
         
         //Class - Customer
         ArrayList<String> correctFnNamesCustomer = new ArrayList<String>( Arrays.asList(
           "public java.lang.String " + packageName + "Customer.getName()",
           "public int " + packageName + "Customer.getBalance()",
           "public " + packageName + "Basket " + packageName + "Customer.getBasket()",
           "public int " + packageName + "Customer.addFunds(int)",
           "public int " + packageName + "Customer.addToBasket(" + packageName + "Reservation)",
           "public int " + packageName + "Customer.addToBasket(" + packageName + "Hotel,java.lang.String,int,boolean)",
           "public int " + packageName + "Customer.addToBasket(" + packageName + "Airport," + packageName + "Airport)",
           "public boolean " + packageName + "Customer.removeFromBasket(" + packageName + "Reservation)",
           "public int " + packageName + "Customer.checkOut()"
           ) );
         ArrayList<String> correctConstNamesCustomer = new ArrayList<String>(Arrays.asList( "public " + packageName + "Customer(java.lang.String,int)"));
         testFnNames(packageName + "Customer", correctFnNamesCustomer, correctConstNamesCustomer);

         // check if the number of fields of a given type are correct
         // Room
         checkField(packageName + "Room", "int", 1, true);
         checkField(packageName + "Room", "java.lang.String", 1, true);
         checkField(packageName + "Room", "boolean", 1, true);

         // HotelReservation
         checkField(packageName + "HotelReservation", packageName +"Hotel", 1, true);
         checkField(packageName + "HotelReservation", "java.lang.String", 1, true);
         checkField(packageName + "HotelReservation", "int", 2, true);
         
         //Hotel
         checkField(packageName + "Hotel", "java.lang.String", 1, true);
         checkField(packageName + "Hotel", "[L" + packageName + "Room;", 1, true);
         
         //Airport
         checkField(packageName + "Airport", "int", 3, true);
         
         //Reservation
         checkField(packageName + "Reservation", "java.lang.String", 1, true);
         
         //FlightReservation
         checkField(packageName + "FlightReservation", packageName + "Airport", 2, true);

         //HotelReservationWithBreakfast
         //no extra fields
         
         //Basket
         checkField(packageName + "Basket", "[L" + packageName + "Reservation;", 1, false);
         
         //Customer
         checkField(packageName + "Customer", "java.lang.String", 1, true);
         checkField(packageName + "Customer", "int", 1, true);
         checkField(packageName + "Customer", packageName + "Basket", 1, true);

         
         // If no-exception is thrown
         System.out.println("Syntax tester passed!");
  }
  catch (Exception e){
   System.out.println(e.getMessage());
  }

 }

}

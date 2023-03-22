package flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;

import logic.DBConnection;
import logic.MainClass;

public class Flight {
	String FlightName;
	Time FromTime;
	Time ToTime;
	Time ReturnFromTime;
	Time ReturnToTime;
	int PriceForBooking;
	String LoggedUser;
	List<String> AvailableSeats;
	public Flight(String flightName, Time fromTime, Time toTime, Time returnFromTime, Time returnToTime,int priceForBooking, String loggedUser, List<String> availableSeats) {
		FlightName = flightName;
		FromTime = fromTime;
		ToTime = toTime;
		ReturnFromTime = returnFromTime;
		ReturnToTime = returnToTime;
		PriceForBooking = priceForBooking;
		LoggedUser = loggedUser;
		AvailableSeats = availableSeats;
	}
	public Flight() {
		
	}
	public void setFlightInfo(String tripMode) {
		Connection con=DBConnection.getDBConnection(); 
		try {
			Statement psmt=con.createStatement();
			ResultSet rs=psmt.executeQuery("select * from FlightDetails");
			if(tripMode.equals("One Way")) {
				System.out.println("S.no \t FlightName \t FromTime \t ToTime \t Price");
			}
			else {
				System.out.println("S.no \t FlightName \t FromTime \t ToTime \t ReturnFromTime \t ReturnToTime \t Price");
			}
			while(rs.next()) {
				if(rs.getTime(5)==null && (tripMode.equals("One Way"))) {
					System.out.println(rs.getInt(1)+" \t "+rs.getString(2)+" \t "+rs.getTime(3)+" \t "+rs.getTime(4)+" \t\t "+rs.getInt(7));
				}
				else if(rs.getTime(5)!=null && tripMode.equals("Round Trip")){
					System.out.println(rs.getInt(1)+" \t "+rs.getString(2)+" \t "+rs.getTime(3)+" \t "+rs.getTime(4)+" \t "+rs.getTime(5)+" \t "+rs.getTime(6)+" \t "+rs.getInt(7));
				}
			}
			
		}
		catch(Exception ex) {
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println(ex.getMessage());
		}
		
	}
	public Flight selectFlight(int FlightNumber,String tripMode,String User,Trip trip) {
		Flight flight=null;
		Connection con=DBConnection.getDBConnection(); 
		try {
			Statement psmt=con.createStatement();
			ResultSet rs=psmt.executeQuery("select * from FlightDetails");
			List <String> temp=null;
			while(rs.next()) {
				if(rs.getInt(1)==FlightNumber && rs.getTime(5)==null && (tripMode.equals("One Way")||tripMode.equals("Multi City"))) {
//					System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getTime(3)+" "+rs.getTime(4)+" "+rs.getTime(5)+" "+rs.getTime(6)+" "+rs.getInt(7)+" "+rs.getString(8));
					//this check whether the BookedFlightDetails contains the following info if so return the list of the table;else return the new array from the flight details
					if((temp=checkBookedFlight(rs.getString(2),trip.getFromDate(),trip.getReturnDate(),rs.getTime(3),rs.getTime(4),rs.getTime(5),rs.getTime(6),trip.getFrom(),trip.getTo()))!=null) {
						flight= new Flight(rs.getString(2),rs.getTime(3),rs.getTime(4),rs.getTime(5),rs.getTime(6),rs.getInt(7),User,temp);
//						System.out.println("if part");
					}else {
						flight= new Flight(rs.getString(2),rs.getTime(3),rs.getTime(4),rs.getTime(5),rs.getTime(6),rs.getInt(7),User,Arrays.asList(rs.getString(8).split(",")));
//						System.out.println("else part");
					}
					
				}
				
				
				else if(rs.getInt(1)==FlightNumber && rs.getTime(5)!=null && tripMode.equals("Round Trip")){
//					System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getTime(3)+" "+rs.getTime(4)+" "+rs.getTime(5)+" "+rs.getTime(6)+" "+rs.getInt(7)+" "+rs.getString(8));
					if((temp=checkBookedFlight(rs.getString(2),trip.getFromDate(),trip.getReturnDate(),rs.getTime(3),rs.getTime(4),rs.getTime(5),rs.getTime(6),trip.getFrom(),trip.getTo()))!=null) {
						flight= new Flight(rs.getString(2),rs.getTime(3),rs.getTime(4),rs.getTime(5),rs.getTime(6),rs.getInt(7),User,temp);
					}else {
						flight= new Flight(rs.getString(2),rs.getTime(3),rs.getTime(4),rs.getTime(5),rs.getTime(6),rs.getInt(7),User,Arrays.asList(rs.getString(8).split(",")));
					}
					
				}
				
			}
		
		}
		catch(Exception ex) {
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println(ex.getMessage()+" selectFLight()");
		}	
		return flight;
	}
	public void setBookedFlightDetails(Flight flight,Trip trip,List<String> AvailableSeatsAfterBooking) {
		Connection con=DBConnection.getDBConnection();
		try {
			PreparedStatement psmt=con.prepareStatement("Insert into BookedFlightDetails values(?,?,?,?,?,?,?,?,?,?)");
			psmt.setString(1, flight.getFlightName());
			psmt.setDate(2, new java.sql.Date(trip.getFromDate().getTime()));
			if(trip.getReturnDate()==null ) {
				psmt.setDate(3,null);
			}
			else {
				psmt.setDate(3, new java.sql.Date(trip.getReturnDate().getTime()));
			}
			psmt.setTime(4, flight.getFromTime());
			psmt.setTime(5, flight.getToTime());
			psmt.setTime(6, flight.getReturnFromTime());
			psmt.setTime(7, flight.getReturnToTime());
			psmt.setString(8, trip.getFrom());
			psmt.setString(9,trip.getTo());
			psmt.setString(10, String.join(",", AvailableSeatsAfterBooking));
			psmt.executeUpdate();
			ResultSet rs=psmt.executeQuery("select * from BookedFlightDetails");
//			while(rs.next()) {
//				System.out.println(rs.getString(1)+" "+rs.getDate(2)+" "+rs.getString(10));
//			}
			}
		catch(Exception ex) {
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println(ex.getMessage()+" setBookedFlightDetails()");
		}
	}
	
	public List<String> checkBookedFlight(String flightName,Date fromDate,Date returnDate,Time fromTime,Time toTime,Time ReturnFromTime,Time ReturnToTime,String from,String to) {
		Connection con=DBConnection.getDBConnection();
		try {
			Statement smt=con.createStatement();
			ResultSet rs=smt.executeQuery("Select * from BookedFlightDetails");
			while(rs.next()) {
//				System.out.println(fromDate+ " "+rs.getDate(2)+" "+fromDate.equals(rs.getDate(2)));
				if(returnDate==null) {
					if(flightName.equals(rs.getString(1)) && fromDate.equals(rs.getDate(2)) && returnDate==rs.getDate(3) && fromTime.equals(rs.getTime(4)) && toTime.equals(rs.getTime(5)) && ReturnFromTime==rs.getTime(6) && ReturnToTime==rs.getTime(7)) {
						return Arrays.asList(rs.getString(10).split(","));
					}
				}
				else {
					if(flightName.equals(rs.getString(1)) && fromDate.equals(rs.getDate(2)) && returnDate.equals(rs.getDate(3)) && fromTime.equals(rs.getTime(4)) && toTime.equals(rs.getTime(5)) && ReturnFromTime.equals(rs.getTime(6)) && ReturnToTime.equals(rs.getTime(7))) {
						return Arrays.asList(rs.getString(10).split(","));
					}
				}
			}
		}catch(Exception ex) {
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println(ex.getMessage()+" CheckBookedFlightDetails()");
		}
		return null;
	}
	public void updateBookedSeats(List<String> seats,Flight flight,Trip trip) {
		Connection con=DBConnection.getDBConnection();
		try {
			//update BookedFlightDetails set AvailableSeats="1,2,3,4,5,6,6," where FlightName="AirIndia" and FromDate="2023-02-12" and ReturnDate=null and FromTime="10:00:00" and ToTime="12:40:00"
			//and ReturnFromTime=null and ReturnToTime=null and TripFrom="Chennai" and TripTo="Thoothukudi";
				if(trip.getTripMode().equals("One Way")) {
					PreparedStatement psmt=con.prepareStatement("Update BookedFlightDetails set AvailableSeats=? where FlightName=? and FromDate=? and FromTime=? and ToTime=? and TripFrom=? and TripTo=?");
					psmt.setString(1, String.join(",", seats));
					psmt.setString(2, flight.getFlightName());
					psmt.setDate(3, new java.sql.Date(trip.getFromDate().getTime()));
						psmt.setTime(4, flight.getFromTime());
						psmt.setTime(5, flight.getToTime());

					
					psmt.setString(6, trip.getFrom());
					psmt.setString(7,trip.getTo());
					int affected=psmt.executeUpdate();
					System.out.println(affected);
					ResultSet rs=psmt.executeQuery("select * from BookedFlightDetails");
//					while(rs.next()) {
//						System.out.println(rs.getString(1)+" "+rs.getDate(2)+" "+rs.getDate(3)+" "+rs.getTime(4)+" "+rs.getTime(5)+" "+rs.getTime(6)+" "+rs.getTime(7)+" "+rs.getString(8)+" "+rs.getString(9)+" "+rs.getString(10));
//					}
				}
					
		else if(trip.getTripMode().equals("Round Trip")) {
			PreparedStatement psmt=con.prepareStatement("Update BookedFlightDetails set AvailableSeats=? where FlightName=? and FromDate=? and FromTime=? and ToTime=? and TripFrom=? and TripTo=? and ReturnDate=?");
			psmt.setString(1, String.join(",", seats));
			psmt.setString(2, flight.getFlightName());
			psmt.setDate(3, new java.sql.Date(trip.getFromDate().getTime()));

				psmt.setTime(4, flight.getFromTime());
				psmt.setTime(5, flight.getToTime());

			
			psmt.setString(6, trip.getFrom());
			psmt.setString(7,trip.getTo());
			psmt.setDate(8,new java.sql.Date(trip.getReturnDate().getTime()));
			int affected=psmt.executeUpdate();
			System.out.println(affected);
			ResultSet rs=psmt.executeQuery("select * from BookedFlightDetails");
//			while(rs.next()) {
//				System.out.println(rs.getString(1)+" "+rs.getDate(2)+" "+rs.getDate(3)+" "+rs.getTime(4)+" "+rs.getTime(5)+" "+rs.getTime(6)+" "+rs.getTime(7)+" "+rs.getString(8)+" "+rs.getString(9)+" "+rs.getString(10));
//			}
		}
		}
		catch(Exception ex) {
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println(ex.getMessage()+" Update BookedSeats");
		}
	}
	
	public String getFlightName() {
		return FlightName;
	}
	public void setFlightName(String flightName) {
		FlightName = flightName;
	}
	public Time getFromTime() {
		return FromTime;
	}
	public void setFromTime(Time fromTime) {
		FromTime = fromTime;
	}
	public Time getToTime() {
		return ToTime;
	}
	public void setToTime(Time toTime) {
		ToTime = toTime;
	}
	public Time getReturnFromTime() {
		return ReturnFromTime;
	}
	public void setReturnFromTime(Time returnFromTime) {
		ReturnFromTime = returnFromTime;
	}
	public Time getReturnToTime() {
		return ReturnToTime;
	}
	public void setReturnToTime(Time returnToTime) {
		ReturnToTime = returnToTime;
	}
	public int getPriceForBooking() {
		return PriceForBooking;
	}
	public void setPriceForBooking(int priceForBooking) {
		PriceForBooking = priceForBooking;
	}
	public String getLoggedUser() {
		return LoggedUser;
	}
	public void setLoggedUser(String loggedUser) {
		LoggedUser = loggedUser;
	}
	public List<String> getAvailableSeats() {
		return AvailableSeats;
	}
	public void setAvailableSeats(ArrayList<String> availableSeats) {
		AvailableSeats = availableSeats;
	}
	//9751263500
	
}

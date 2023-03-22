package flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Level;

import logic.DBConnection;
import logic.MainClass;
import users.Passenger;

public class Tickets {
	Passenger user=null;
	Flight flight=null;
	Trip trip=null;
	Seats seat=null;
	List<String> bookedSeat=null;
	List<String> returnBookedSeat=null;
	int economyPrice;
	
	public Tickets(Passenger user, Flight flight, Trip trip, Seats seat, List<String> bookedSeat,List<String> returnBookedSeat,int economy) {
		super();
		this.user = user;
		this.flight = flight;
		this.trip = trip;
		this.seat = seat;
		this.bookedSeat = bookedSeat;
		this.returnBookedSeat = returnBookedSeat;
		this.economyPrice=economy;
	}
	public Tickets() {
	}
	public void showTickets() {
		if(trip.getTripMode().equals("One Way")) {
			System.out.println("-------------------------------------------Your Ticket---------------------------------------------------\n");
			System.out.println("Name: "+user.getPassengerName()+"\tMobile Number: "+user.getMobileNumber()+"\tFrom:  "+trip.getFrom()+"\tTo: "+trip.getTo()+"\tTripMode: One Way"+
					"\n\nDate of Departure: "+new SimpleDateFormat("dd/MM/yyyy").format(trip.getFromDate())+"\tClass: "+trip.getEconomy()+"\tFlight Name: "+flight.getFlightName()+
				"\n\nBooked Seats: "+bookedSeat+"\tAdultCount: "+seat.getAdultCount()+"\tChildCount: "+seat.getChildCount()+
				"\n\nPriceForBooning: "+flight.getPriceForBooking()+
				"\n\nEconomyPrice: "+economyPrice+
					"\n\nTotal Price For Booing: "+(flight.getPriceForBooking()+economyPrice)*(seat.getAdultCount()+seat.getChildCount()));
		}else {
			System.out.println("-------------------------------------------Your Ticket---------------------------------------------------\n");
			System.out.println("Name: "+user.getPassengerName()+"\tMobile Number: "+user.getMobileNumber()+"\tFrom:  "+trip.getFrom()+"\tTo: "+trip.getTo()+
					"\n\nDate of Departure: "+new SimpleDateFormat("dd/MM/yyyy").format(trip.getFromDate())+"\tDate of Arrival: "+new SimpleDateFormat("dd/MM/yyyy").format(trip.getReturnDate())+"\tClass: "+trip.getEconomy()+"\tFlight Name: "+flight.getFlightName()+
				"\n\nBooked Seats: "+bookedSeat+"\tReturn Booked Seats: "+returnBookedSeat+"\tAdultCount: "+seat.getAdultCount()+"\tChildCount: "+seat.getChildCount()+
				"\n\nPriceForBooning: "+flight.getPriceForBooking()+
				"\n\nEconomyPrice: "+economyPrice+
					"\n\nTotal Price For Booing: "+(flight.getPriceForBooking()+economyPrice)*(seat.getAdultCount()+seat.getChildCount()));
		}
		
	}
	public void SetTicket(){
		Connection con=DBConnection.getDBConnection();
		try {
			PreparedStatement psmt=con.prepareStatement("insert into Tickets values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			psmt.setString(1, user.getPassengerName());
			psmt.setString(2 ,user.getMobileNumber());
			psmt.setString(3, flight.getFlightName());
			psmt.setString(4, trip.getFrom());
			psmt.setString(5, trip.getTo());
			
			psmt.setDate(6,new java.sql.Date(trip.getFromDate().getTime()));
			if(trip.getReturnDate()==null) {
				psmt.setDate(7,null);
			}
			else {
				psmt.setDate(7,new java.sql.Date(trip.getReturnDate().getTime()));
			}
			
			psmt.setTime(8,flight.getFromTime());
			psmt.setTime(9,flight.getToTime());
			psmt.setTime(10,flight.getReturnFromTime());
			psmt.setTime(11,flight.getReturnToTime());
			psmt.setString(12, ""+trip.getEconomy());
			psmt.setInt(13, seat.getAdultCount());
			psmt.setInt(14, seat.getChildCount());
			psmt.setInt(15, flight.getPriceForBooking());
			psmt.setInt(16, economyPrice);
			psmt.setString(17, String.join(",", bookedSeat));
			if(returnBookedSeat==null) {
				psmt.setString(18,null);
			}
			else {
				psmt.setString(18,String.join(",", returnBookedSeat));
			}
		
			psmt.executeUpdate();
			ResultSet rs=psmt.executeQuery("Select * from Tickets");
//			while(rs.next()) {
//				System.out.println(rs.getString(1));
//			}
		}
		catch(Exception ex) {
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println(ex.getMessage());
		}
		
	}
	public void ViewTicket(Passenger user) {
		Connection con=DBConnection.getDBConnection();
		try {
			
		Statement smt=con.createStatement();
		ResultSet rs=smt.executeQuery("Select * from Tickets");
		while(rs.next()) {
			if(user.getPassengerName().equals(rs.getString(1))) {
				if(rs.getDate(7)!=null) {
					System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
					System.out.println("Name: "+rs.getString(1)+"\tMobile Number: "+rs.getString(2)+"\tFrom:  "+rs.getString(4)+"\tTo: "+rs.getString(5)+
							"\n\nDate of Departure: "+rs.getDate(6)+"\tClass: "+rs.getString(12)+"\tFlight Name: "+rs.getString(3)+
							"\n\nDepature: "+rs.getTime(8)+"\tArrival: "+rs.getTime(9)+"\tTripMode: Round Trip"+
							"\n\nBooked Seats: "+Arrays.asList(rs.getString(17))+"\tReturn Booked Seats:"+Arrays.asList(rs.getString(18)) +"\tAdultCount: "+rs.getInt(13)+"\tChildCount: "+rs.getInt(14)+"\tTotal Count: "+rs.getInt(13)+rs.getInt(14)+
							"\n\nPriceForBooning: "+rs.getInt(15)+"\tEconomyPrice: "+rs.getInt(16)+"\tTotalPrice: "+(rs.getInt(15)+rs.getInt(16))*(rs.getInt(13)+rs.getInt(14)));
					System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
				}
				else {
					System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
					System.out.println("Name: "+rs.getString(1)+"\tMobile Number: "+rs.getString(2)+"\tFrom:  "+rs.getString(4)+"\tTo: "+rs.getString(5)+
							"\n\nDate of Departure: "+rs.getDate(6)+"\tClass: "+rs.getString(12)+"\tFlight Name: "+rs.getString(3)+
							"\n\nDepature: "+rs.getTime(8)+"\tArrival: "+rs.getTime(9)+"\tTripMode: One Way"+
							"\n\nBooked Seats: "+Arrays.asList(rs.getString(17))+"\tAdultCount: "+rs.getInt(13)+"\tChildCount: "+rs.getInt(14)+"\tTotal Count: "+rs.getInt(13)+rs.getInt(14)+
							"\n\nPriceForBooning: "+rs.getInt(15)+"\tEconomyPrice: "+rs.getInt(16)+"\tTotalPrice: "+(rs.getInt(15)+rs.getInt(16))*(rs.getInt(13)+rs.getInt(14)));
					System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
				}
				
			}
		
		}
		}
		catch(Exception ex) {
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println(ex.getMessage());
		}
	}
	 public void ShowBookedTicket() {

			Connection con=DBConnection.getDBConnection();
			try {
				
			Statement smt=con.createStatement();
			ResultSet rs=smt.executeQuery("Select * from Tickets");
			while(rs.next()) {
					if(rs.getDate(7)!=null) {
						System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
						System.out.println("Name: "+rs.getString(1)+"\tMobile Number: "+rs.getString(2)+"\tFrom:  "+rs.getString(4)+"\tTo: "+rs.getString(5)+
								"\n\nDate of Departure: "+new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate(6))+"\tClass: "+rs.getString(12)+"\tFlight Name: "+rs.getString(3)+
								"\n\nDepature: "+rs.getTime(8)+"\tArrival: "+rs.getTime(9)+"\tTripMode: Round Trip"+
								"\n\nBooked Seats: "+Arrays.asList(rs.getString(17))+"\tReturn Booked Seats:"+Arrays.asList(rs.getString(18)) +"\tAdultCount: "+rs.getInt(13)+"\tChildCount: "+rs.getInt(14)+"\tTotal Count: "+rs.getInt(13)+rs.getInt(14)+
								"\n\nPriceForBooning: "+rs.getInt(15)+"\tEconomyPrice: "+rs.getInt(16)+"\tTotalPrice: "+(rs.getInt(15)+rs.getInt(16))*(rs.getInt(13)+rs.getInt(14)));
						System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
					}
					else {
						System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
						System.out.println("Name: "+rs.getString(1)+"\tMobile Number: "+rs.getString(2)+"\tFrom:  "+rs.getString(4)+"\tTo: "+rs.getString(5)+
								"\n\nDate of Departure: "+new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate(6))+"\tClass: "+rs.getString(12)+"\tFlight Name: "+rs.getString(3)+
								"\n\nDepature: "+rs.getTime(8)+"\tArrival: "+rs.getTime(9)+"\tTripMode: One Way"+
								"\n\nBooked Seats: "+Arrays.asList(rs.getString(17))+"\tAdultCount: "+rs.getInt(13)+"\tChildCount: "+rs.getInt(14)+"\tTotal Count: "+rs.getInt(13)+rs.getInt(14)+
								"\n\nPriceForBooning: "+rs.getInt(15)+"\tEconomyPrice: "+rs.getInt(16)+"\tTotalPrice: "+(rs.getInt(15)+rs.getInt(16))*(rs.getInt(13)+rs.getInt(14)));
						System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
					}
				}
			}
			catch(Exception ex) {
				MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
				System.out.println(ex.getMessage());
			}
		
	 }
	
}

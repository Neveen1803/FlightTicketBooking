package flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;

import logic.DBConnection;
import logic.MainClass;

public class Seats {
	String LoggedUser;
	String FlightName;

	int AdultCount;
	int ChildCount;
	int TotalCount;
	List<String> BookedSeats;
	public Seats(String loggedUser, String flightName,  int adultCount, int childCount, int totalCount,List<String>bookedSeats) {
		LoggedUser = loggedUser;
		FlightName = flightName;
		AdultCount = adultCount;
		ChildCount = childCount;
		TotalCount = totalCount;
		BookedSeats = bookedSeats;
		
	}
	public void setSeats(Seats seat) {
		Connection con=DBConnection.getDBConnection();
		try {
			PreparedStatement psmt=con.prepareStatement("insert into Seats values(?,?,?,?,?,?)");
			psmt.setString(1, seat.getLoggedUser());
			psmt.setString(2,seat.getFlightName());
			psmt.setInt(3, seat.getAdultCount());
			psmt.setInt(4,seat.getChildCount() );
			psmt.setInt(5, seat.getTotalCount());
			psmt.setString(6,String.join(",",seat.getBookedSeats()));
			psmt.executeUpdate();
			ResultSet rs=psmt.executeQuery("select * from Seats");
//			while(rs.next()) {
//				System.out.println(rs.getString(1)+""+rs.getString(2)+" " +rs.getInt(3)+" "+rs.getInt(4)+""+rs.getString(6));
//			}
		}
		catch(Exception ex) {
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println(ex.getMessage());
		}
	}
	public String getLoggedUser() {
		return LoggedUser;
	}
	public void setLoggedUser(String loggedUser) {
		LoggedUser = loggedUser;
	}
	public String getFlightName() {
		return FlightName;
	}
	public void setFlightName(String flightName) {
		FlightName = flightName;
	}
	public int getAdultCount() {
		return AdultCount;
	}
	public void setAdultCount(int adultCount) {
		AdultCount = adultCount;
	}
	public int getChildCount() {
		return ChildCount;
	}
	public void setChildCount(int childCount) {
		ChildCount = childCount;
	}
	public int getTotalCount() {
		return TotalCount;
	}
	public void setTotalCount(int totalCount) {
		TotalCount = totalCount;
	}
	public List<String> getBookedSeats() {
		return BookedSeats;
	}
}

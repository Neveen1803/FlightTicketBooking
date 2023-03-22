package flight;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Date;

import org.apache.log4j.Level;

import enums.Economy;
import logic.DBConnection;
import logic.MainClass;
public class Trip {
	String LoggedUser;
	String From;
	String To;
	Date FromDate;
	Date ReturnDate;
	String TripMode;
	Economy Economy;
	public Trip(String loggedUser,String from,String to,Date fromDate,Date returnDate,String trioMode,Economy economy) {
		LoggedUser=loggedUser;
		From=from;
		To=to;
		FromDate=fromDate;
		ReturnDate=returnDate;
		TripMode=trioMode;
		Economy=economy;
	}
	public Trip() {
		
	}

	public String SelectFrom(int from) {
		return (from==1)?"Chennai":(from==2)?"Thoothukudi":(from==3)?"Madurai":(from==4)?"Kayathar":(from==5)?"Coimbatore":(from==6)?"Salem":(from==7)?"Vellore":(from==8)?"Puducherry":"Invalid";
		}	
	
	public String SelectTo(int to) {
		return (to==1)?"Chennai":(to==2)?"Thoothukudi":(to==3)?"Madurai":(to==4)?"Kayathar":(to==5)?"Coimbatore":(to==6)?"Salem":(to==7)?"Vellore":(to==8)?"Puducherry":"Invalid";
	}
	public void selectDate(){
		
	}
	
	
	public void setTripInfo() {
		Connection con=DBConnection.getDBConnection();
		try {
			PreparedStatement psmt=con.prepareStatement("Insert into Trip values(?,?,?,?,?,?,?)");
			psmt.setString(1,getLoggedUser());
			psmt.setString(2,getFrom());
			psmt.setString(3,getTo());
			psmt.setDate(4,new java.sql.Date(FromDate.getTime()));
			if(ReturnDate==null) {
				psmt.setDate(5,null);
			}
			else {
				psmt.setDate(5,new java.sql.Date(ReturnDate.getTime()));
			}
			psmt.setString(6,getTripMode());
			psmt.setString(7,getEconomy().name());
			psmt.executeUpdate();
			ResultSet rs=psmt.executeQuery("Select * from Trip");
//			while(rs.next()) {//this should be removed
//				System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getDate(4)+" "+rs.getDate(5)+" "+rs.getString(6)+" "+rs.getString(7));
//			}//
		}
		catch(Exception ex) {
			ex.printStackTrace();
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println("ValidateSignUp getDBConnection");
		}
	}
	public String getLoggedUser() {
		return LoggedUser;
	}
	public void setLoggedUser(String loggedUser) {
		LoggedUser = loggedUser;
	}
	public String getFrom() {
		return From;
	}
	public void setFrom(String from) {
		From = from;
	}
	public String getTo() {
		return To;
	}
	public void setTo(String to) {
		To = to;
	}
	public Date getFromDate() {
		return FromDate;
	}
	public void setFromDate(Date fromDate) {
		FromDate = fromDate;
	}
	public Date getReturnDate() {
		return ReturnDate;
	}
	public void setReturnDate(Date returnDate) {
		ReturnDate = returnDate;
	}
	public String getTripMode() {
		return TripMode;
	}
	public void setTripMode(String tripMode) {
		TripMode = tripMode;
	}
	public Economy getEconomy() {
		return Economy;
	}
	public void setEconomy(Economy economy) {
		Economy = economy;
	}
	
}

package users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import org.apache.log4j.Level;

import logic.DBConnection;
import logic.MainClass;

public class Passenger {
	private String PassengerId;
	private String PassengerName;
	private String PassengerPassword;
	private String MobileNumber;
	public Passenger(String name,String password,String mobileNumber,String id) {
		PassengerId=id;
		PassengerName=name;
		PassengerPassword=password;
		MobileNumber=mobileNumber;
		
	}
	public void setSignUp() {
		Connection con=DBConnection.getDBConnection();
		try {
			PreparedStatement psmt=con.prepareStatement("Insert into User values(?,?,?,?,?)");
			psmt.setString(1,getPassengerName());
			psmt.setString(2,getPassengerPassword());
			psmt.setString(3,getMobileNumber());
			psmt.setString(4,getPassengerId());
			psmt.setString(5,"User");
			psmt.executeUpdate();
			ResultSet rs=psmt.executeQuery("Select * from User");
//			while(rs.next()) {//this should be removed
//				System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
//			}//
		}
		catch(Exception ex) {
		
			MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
			System.out.println("ValidateSignUp getDBConnection");
		}
	}
	public String getPassengerId() {
		return PassengerId;
	}
	public void setPassengerId(String passengerId) {
		PassengerId = passengerId;
	}
	public String getPassengerName() {
		return PassengerName;
	}
	public void setPassengerName(String passengerName) {
		PassengerName = passengerName;
	}
	public String getPassengerPassword() {
		return PassengerPassword;
	}
	public void setPassengerPassword(String passengerPassword) {
		PassengerPassword = passengerPassword;
	}
	public String getMobileNumber() {
		return MobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
	
}

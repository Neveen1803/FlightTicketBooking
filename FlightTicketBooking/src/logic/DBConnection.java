package logic;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;

import org.apache.log4j.Level;
public class DBConnection {
static Connection con=null;
private DBConnection() {
}
public static Connection getDBConnection() {
	try {
		if(con==null) {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost/FlightTicketBooking","neveen-zstk305","jHu_UpMRi3u");
		}
	}
	catch(Exception ex) {
		ex.printStackTrace();
		System.out.println("Exception getBDConnection Singleton");
		MainClass.log.log(Level.ERROR,MainClass.userId+" "+LocalDateTime.now()+" "+ex);
	}
	return con;
}
}

package logic;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import enums.Economy;
import flight.Flight;
import flight.Seats;
import flight.Tickets;
import flight.Trip;
import users.Passenger;

public class MainClass {
	public static Logger log=Logger.getLogger(MainClass.class.getName());
	Passenger user=null;
	Trip trip=null;
	Flight flight=null;
	Seats seat=null;
	List<String> seats;
	List<String> Chosenseats;
	public static String userId=null;
	public static String User;
	static {
		System.out.println("-------------------------------------------Flight Ticket Booking-------------------------------------------\n");
	}
	public static void main(String[] args) {
		 log.log(Level.DEBUG,  LocalDateTime.now()+" entered main class");
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		int choice=0;
		MainClass main=new MainClass();
		System.out.println("1-> SignUp\n2-> SignIn");
		try {
			choice=sc.nextInt();
			sc.nextLine();
		}
		catch(Exception ex) {
			System.out.println("invalid input");
			 log.log(Level.ERROR,  LocalDateTime.now()+" "+ex);
		}
		if(choice==1) {
			//Signup
			while(true) {
				String UserName,Password,MobileNumber;
				//this loop get the name until the format is correct
				while(true) {
					 log.log(Level.DEBUG,  LocalDateTime.now()+" getting the user name");
					System.out.println("Enter the UserName");
					UserName=sc.nextLine();
					if(main.validateName(UserName)) {
						break;
					}
					 log.log(Level.ERROR,  LocalDateTime.now()+" Invalid User Name. Please enter the user Name without special character and number");

					System.out.println("Invalid User Name. Please enter the user Name without special character and number");
				}
				//this loop get the password until the give format is correct
				while(true) {
					 log.log(Level.DEBUG,  LocalDateTime.now()+" getting password");
					System.out.println("1.At least one upper case English letter,\n"
							+ "At least one lower case English letter,\n"
							+ "At least one digit,\n"
							+ "At least one special character,\n"
							+ "Minimum eight in length .\n");
					System.out.println("Enter the PassWord");
					Password=sc.nextLine();
					if(main.validatePassword(Password)) {
						break;
					}
					 log.log(Level.ERROR,  LocalDateTime.now()+" Invalid PassWord");
					System.out.println("Invalid PassWord");
				}
				
				//this loop check the mobile number until the give format is correct
				while(true) {
					 log.log(Level.DEBUG,  LocalDateTime.now()+" getting Mobile Number");
					System.out.println("Enter the Mobile Number");
					MobileNumber=sc.nextLine();
					if(main.validateNumber(MobileNumber)) {
						break;
					}
					 log.log(Level.ERROR,  LocalDateTime.now()+" Invalid Mobile Number The number should hava 10 digits and no letter and special character are accepted");
					System.out.println("Invalid Mobile Number The number should hava 10 digits and no letter and special character are accepted");
				}
			
				String UserId=UserName+"@"+MobileNumber.substring(0, 4);
				
				
				if(main.ValidateSignUp(UserName,Password,MobileNumber,UserId)) {
					log.log(Level.DEBUG,  LocalDateTime.now()+"Passing parameter to signin");
					main.user=new Passenger(UserName,Password,MobileNumber,UserId);
					main.user.setSignUp();
					log.log(Level.DEBUG,main.user.getPassengerId()+" "+LocalDateTime.now()+" Success fully signed");
					System.out.println("Success fully signed");
					break;
				}
				else {
					System.out.println("The user has already exist with same details");
					log.log(Level.ERROR,LocalDateTime.now()+" The user has already exist with same details");
				}
				
			}
			
		}
		
		else if(choice==2) {
			//signin
			log.log(Level.DEBUG,  LocalDateTime.now()+"Siging in");
			while(true) {
				String Name,Password;
				//username
				//this loop check the name until the give format is correct
				while(true) {
					System.out.println("Enter the UserName");
					Name=sc.nextLine();
					if(main.validateName(Name)) {
						break;
					}
					System.out.println("Invalid User Name. Please enter the user Name without special character and number");
				}
				//password
				//this loop check the password until the give format is correct
				while(true) {
					System.out.println("Enter the PassWord");
					Password=sc.nextLine();
					if(main.validatePassword(Password)) {
						break;
					}
					System.out.println("Invalid PassWord");
				}
				if(main.ValidateSignIn(Name, Password)) {
					userId=main.user.getPassengerId();
					log.log(Level.DEBUG,main.user.getPassengerId()+" "+LocalDateTime.now()+" Successfully signed in...");
					System.out.println("Successfully signed in...");
					break;
				}
				log.log(Level.ERROR,LocalDateTime.now()+" User not found");
				System.out.println("User not found");
			}
		}
		else {
			main(new String[] {"1"});
		}
		if(main.User.equals("Admin")) {
			System.out.println("Admin");
			///Admin Flow
			while(true) {
				while(true) {
					System.out.println("1->Update Time of Flight\n2->View Booked Flight Details\nAny key to exit");
					while(true) {
					try {
						choice=sc.nextInt();
						if(choice==1 || choice==2) {
							break;
						}
						else {
							System.out.println("There is only two option!");
						}
						}
						catch(Exception ex) {
						
							sc.nextLine();
							log.log(Level.ERROR,main.user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
							main(new String[] {"1"});
						}
				}
					if(choice==1) {
						int TripMode=0;
						while(true) {
							try {
								System.out.println("1->OneWay\n2->RoundTrip");
								TripMode=sc.nextInt();
							
								 if(TripMode==1 || TripMode==2) {
										break;
									}
									else {
										System.out.println("There is only two option!");
									}
							}
							catch(Exception ex) {
								System.out.println("Invalid Input");
								sc.nextLine();
								log.log(Level.ERROR,main.user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
							}
						}
						if(TripMode==1) {
							main.Update("One Way");
							
						}
						else if(TripMode==2) {
							main.Update("RoundTrip");
						}
					}
					else if(choice==2) {
						///////////new Tickets().ViewTicket(main.user);
						new Tickets().ShowBookedTicket();
					}
					
				}
			
			}
		}
		else {
			System.out.println("User");
			while(true) {
				log.log(Level.DEBUG,main.user.getPassengerId()+" "+LocalDateTime.now()+" getting User choice");
				System.out.println("1->View Booked Tickets\n2->Book Ticket\n Any key to exit");
				while(true) {
					try {
						choice=sc.nextInt();
						if(choice==1 || choice==2) {
							break;
						}
						else {
							System.out.println("There is only two option!");
						}
						}
						catch(Exception ex) {
							main(new String[] {"1"});
							sc.nextLine();
							log.log(Level.ERROR,main.user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
						}
				}
				
				if(choice==1) {
					//view Booked Tickets
					new Tickets().ViewTicket(main.user);
				
				}
				else if(choice==2) {
					//book ticket
					int tripChoice;
					while(true) {
						try {
							System.out.println("1->OneWay\n2->RoundTrip");
							 tripChoice=sc.nextInt();
							 if(tripChoice==1 || tripChoice==2) {
									break;
								}
								else {
									System.out.println("There is only two option!");
								}
						}
						catch(Exception ex) {
							System.out.println("Invalid Input");
							sc.nextLine();
							log.log(Level.ERROR,main.user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
						}
					}
					
					if(tripChoice==1) {
						//one way
						main.OneWay();
						
					}
					else if(tripChoice==2) {
						//round trip
						System.out.println("Round trip");
						main.RoundTrip();
					}
					
				}
//				else if(choice==3) {
//					//cancel ticket
//				}
			}
		}
	
		
		
	
	
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void OneWay() {
		String From,To,Trip;
		int Adults,Children,TotalCount,economyPrice;
		Economy economy;
		trip=new Trip();
		Date FromDate;
		Scanner input=new Scanner(System.in);
		//From
		while (true) {
			 try {
				 System.out.println("1. Chennnai\t2. Thoothukudi\t3. Madurai\t4. Kayathar\n5. Coimbatore\t6. Salem\t7. Vellore\t8. Puducherry");
				 System.out.println("Where From?(enter from 1-8)");
				 int from=input.nextInt();
				 if(from>0 && from<=8){
					From=trip.SelectFrom(from);
					trip.setFrom(From);
					break;
				   }
				 else {
						System.out.println("The given value is not in range");
					 }
			  }
			  catch(Exception ex) {
				 System.out.println("Invalid input select from");
				 input.nextLine();
					log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
			  }
	}
		//To
		while (true) {
			 try {
				 System.out.println("1. Chennnai\t2. Thoothukudi\t3. Madurai\t4. Kayathar\n5. Coimbatore\t6. Salem\t7. Vellore\t8. Puducherry");
				 System.out.println("Where To?(enter from 1-8)");
				 int to=input.nextInt();
				 input.nextLine();
				 if(to>0 && to<=8){
					To=trip.SelectTo(to);
					trip.setTo(To);
					break;
				   }
				 else {
						System.out.println("The given value is not in range");
					 }
			  }
			  catch(Exception ex) {
				 System.out.println("Invalid input select from");
				 input.nextLine();
				 log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
			  }
	}
		if(trip.getFrom().equals(trip.getTo())) {
			OneWay();
		}
		
		//selectDate
		
		Calendar cal=Calendar.getInstance();
		   Date currentDate=cal.getTime();
		   
		   while (true) {
			   System.out.println("Enter the departure date(dd/MM/yyyy)");
			String date=  input.nextLine();
		   SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		   try {
			FromDate=sdf.parse(date);
			if(FromDate.after(currentDate)){//when the date is after current date you get the departure date .
				break;
			}
			else{
				System.out.println("You can't select the date for departure today and before today.");
			}
		} catch (Exception e) {
			System.out.println("The date format is wrong.Please give a valid date");
			log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+e);
			
		}
		   }
		   
		   //selectEconomy
		   while(true) {
			   System.out.println("1->Premium\n2->FirstClass\n3->Bussiness\n4->Economy");
			   try {
				   int economychoice=input.nextInt();
				   if(economychoice==1) {
						 economy= Economy.Premium;
						 economyPrice=2000;
						 break;
					 }
					 else if(economychoice==2)  {
						 economy= Economy.FirstClass;
						 economyPrice=1500;

						 break;
					 }
					 else if(economychoice==3) {
						 economy= Economy.Bussiness;
						 economyPrice=1000;

						 break;
					 }
					 else if(economychoice==4) {
						 economy=Economy.Economy;
						 economyPrice=500;

						 break;
					 }
					 else {
						 System.out.println("Not a valid Input");
						 continue;
					 }
			   }
			   catch(Exception ex) {
				   System.out.println("Not a valid input.Sepecial character or alphabets are not allowed! ");
				   input.nextLine();
				   log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
			   }
			   
			   
		   }
		   
		trip=new Trip(user.getPassengerId(), From, To, FromDate, null, "One Way", economy);
		trip.setTripInfo();
		flight=new Flight();
		flight.setFlightInfo("One Way");
		int FlightNumber=0;
		
		while(true) {
			System.out.println("Select the flight with Flight Number");
			try {
				FlightNumber=input.nextInt();
				input.nextLine();
				if(FlightNumber<6 && FlightNumber>0) {
					break;
				}
				else {
					continue;
				}
				}
			catch(Exception ex) {
				System.out.println("Error in getting flightNumber in One Way");
				log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
			}
		}
	flight=flight.selectFlight(FlightNumber,"One Way",user.getPassengerId(),trip);//choosed flight	
	
	seats=new ArrayList<String>();
	Chosenseats=new ArrayList<String>();
	seats.addAll(flight.getAvailableSeats());
//	System.out.println(seats);
		
		while(true) {
			while(true) {
				System.out.println("Enter the adultCount");
				try {
					Adults=input.nextInt();
					input.nextLine();
					if(Adults>0) {
						break;
					}
					else {
						System.out.println("Invalid number in adult count can't be zero");
					}
				}
				catch(Exception ex) {
					System.out.println(ex.getMessage());
					log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
				}
			}
			while(true) {
				System.out.println("Enter the Children Count");
				try {
					Children=input.nextInt();
					input.nextLine();
					if(Children>=0) {
						break;
					}
					else {
						System.out.println("Invalid number in adult count can't be zero");
					}
				}
				catch(Exception ex) {
					System.out.println(ex.getMessage());
					log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
				}
			}
			TotalCount=Adults+Children;
			if(TotalCount<0 || TotalCount>30) {
				System.out.println("There is no enough space in the flight");
				continue;
			}
			else {
				break;
			}
		}
		while(true) {

			
			
			while(TotalCount>0) {
				int index=0;
				
				for(int i=0;i<5;i++) {
					for(int j=0;j<6;j++) {
						System.out.print(seats.get(index)+"\t");//index out of bound
						index++;
					}
					System.out.println();
					System.out.println();
				}
				System.out.println("Choose the seats");
				String choosedseats=input.nextLine();
				String Expression="^[0-9]{1,2}$";
				try {
					int ValueChecker=Integer.parseInt(choosedseats);
					if(seats.contains(choosedseats) && choosedseats.matches(Expression) && ValueChecker<=30) {
						Chosenseats.add(choosedseats);
						seats.set(seats.indexOf(choosedseats), "X");
						TotalCount-=1;
					}
					else if(Chosenseats.contains(choosedseats)) {
						System.out.println("seat has already been chosen. Please try Again with Some Other seats.");
						continue;
					}
					else {
						System.out.println("The choosen seat is not availabale!!!");
						continue;
					}
				}
				catch(Exception ex) {
					System.out.println("Invalid Input");
					log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
					continue;
					
				}
			}
		break;
		}
//		System.out.println(seats);
//		System.out.println(Chosenseats);
		
		//flightname,fromdate,TOdate,FromTime,ToTime,Availabke seats,
		flight.setBookedFlightDetails(flight,trip, seats);
		flight.updateBookedSeats(seats,flight,trip);
		seat=new Seats(trip.getLoggedUser(),flight.getFlightName(),Adults,Children,TotalCount,Chosenseats);
		seat.setSeats(seat);
		Tickets ticket=new Tickets(user, flight, trip, seat, Chosenseats, null,economyPrice);
		ticket.SetTicket();
		ticket.showTickets();
		//------------------------------------------------------------------------------------------------------------------------------------------------
		//After this send the Date to the database and then choose the flight details
		
//		System.out.println("done");
		
		//set the data for the seat and athen show the ticket
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void RoundTrip() {
		String From,To,Trip;
		Economy economy;
		int Adults,Children,TotalCount,economyPrice;
		Date FromDate,ReturnDate;
		trip=new Trip();
		Scanner input=new Scanner(System.in);
		//From
		while (true) {
			 try {
				 System.out.println("1. Chennnai\t2. Thoothukudi\t3. Madurai\t4. Kayathar\n5. Coimbatore\t6. Salem\t7. Vellore\t8. Puducherry");
				 System.out.println("Where From?(enter from 1-8)");
				 int from=input.nextInt();
				 if(from>0 && from<=8){
					From=trip.SelectFrom(from);
					trip.setFrom(From);
					break;
				   }
				 else {
						System.out.println("The given value is not in range");
					 }
			  }
			  catch(Exception ex) {
				 System.out.println("Invalid input select from");
				 input.nextLine();
				 log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
			  }
	}
		//To
		while (true) {
			 try {
				 System.out.println("1. Chennnai\t2. Thoothukudi\t3. Madurai\t4. Kayathar\n5. Coimbatore\t6. Salem\t7. Vellore\t8. Puducherry");
				 System.out.println("Where To?(enter from 1-8)");
				 int to=input.nextInt();
				 input.nextLine();
				 if(to>0 && to<=8){
					To=trip.SelectTo(to);
					trip.setTo(To);
					break;
				   }
				 else {
						System.out.println("The given value is not in range");
					 }
			  }
			  catch(Exception ex) {
				 System.out.println("Invalid input select from");
				 input.nextLine();
				 log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
			  }
	}
		if(trip.getFrom().equals(trip.getTo())) {
			RoundTrip();
		}
		
		//selectDate
		
		Calendar cal=Calendar.getInstance();
		   Date currentDate=cal.getTime();
		   
		   while (true) {
			   System.out.println("Enter the departure date(dd/MM/yyyy)");
			String date=  input.nextLine();
		   SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		   try {
			FromDate=sdf.parse(date);
			if(FromDate.after(currentDate)){//when the date is after current date you get the departure date .
				break;
			}
			else{
				System.out.println("You can't select the date for departure today and before today.");
			}
		} catch (Exception e) {
			System.out.println("The date format is wrong.Please give a valid date");
			log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+e);
			
		}
		   }
		   //selectReturn Date
		   while (true) {
			   System.out.println("Enter the Return date(dd/MM/yyyy)");
			String date=  input.nextLine();
		 
		   SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		   try {
			ReturnDate=sdf.parse(date);
			trip.setReturnDate(ReturnDate);
			if(ReturnDate.after(FromDate)){
				break;
			}
			else{
				System.out.println("The date is not after the from date (Return date)");
			}
		} catch (Exception e) {
			System.out.println("Invalid Input");
			log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+e);
			
		}
		   }
		   //selectEconomy
		   while(true) {
			   System.out.println("1->Premium\n2->FirstClass\n3->Bussiness\n4->Economy");
			   try {
				   int economychoice=input.nextInt();
				   if(economychoice==1) {
						 economy= Economy.Premium;
						 economyPrice=2000;
						 break;
					 }
					 else if(economychoice==2)  {
						 economy= Economy.FirstClass;
						 economyPrice=1500;
						 break;
					 }
					 else if(economychoice==3) {
						 economy= Economy.Bussiness;
						 economyPrice=1000;
						 break;
					 }
					 else if(economychoice==4) {
						 economy=Economy.Economy;
						 economyPrice=500;
						 break;
					 }
					 else {
						 System.out.println("Not a valid Input");
						 continue;
					 }
			   }
			   catch(Exception ex) {
				   System.out.println("Not a valid input.Sepecial character or alphabets are not allowed! ");
				   input.nextLine();
				   log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
			   }	   
		   }
		   
		trip=new Trip(user.getPassengerId(), From, To, FromDate, ReturnDate, "Round Trip", economy);
		trip.setTripInfo();
		flight=new Flight();
		flight.setFlightInfo("Round Trip");
	//After this send the Date to the database and then choose the flight details
		int FlightNumber=0;
		System.out.println("Select the flight with Flight Number");
		while(true) {
			try {
				FlightNumber=input.nextInt();
				input.nextLine();
				if(FlightNumber>0 && FlightNumber<6) {
					break;
				}
				System.out.println("Flight number 1-5");
				}
			catch(Exception ex) {
				System.out.println("Error in getting flightNumber in One Way");
				log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
			}
		}
		flight=flight.selectFlight(FlightNumber,"Round Trip",user.getPassengerId(),trip);//choosed flight	
		
		seats=new ArrayList<String>();
		Chosenseats=new ArrayList<String>();
		seats.addAll(flight.getAvailableSeats());
//		System.out.println(seats);
			//getting the count for seats
			while(true) {
				while(true) {
					System.out.println("Enter the adultCount");
					try {
						Adults=input.nextInt();
						input.nextLine();
						if(Adults>0) {
							break;
						}
						else {
							System.out.println("Invalid number in adult count can't be zero");
						}
					}
					catch(Exception ex) {
						System.out.println(ex.getMessage());
						log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
					}
				}
				while(true) {
					System.out.println("Enter the Child Count");
					try {
						Children=input.nextInt();
						input.nextLine();
						if(Children>=0) {
							break;
						}
						else {
							System.out.println("Invalid number in adult count can't be zero");
						}
					}
					catch(Exception ex) {
						System.out.println(ex.getMessage());
						log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
					}
				}
				
				TotalCount=Adults+Children;
				if(TotalCount<0 || TotalCount>30) {
					System.out.println("There is no enough space in the flight");
					continue;
				}
				else {
					break;
				}
			}
			while(true) {
				
				while(TotalCount>0) {
					int index=0;
					
					for(int i=0;i<5;i++) {
						for(int j=0;j<6;j++) {
							System.out.print(seats.get(index)+"\t");//index out of bound
							index++;
						}
						System.out.println();
						System.out.println();
					}
					System.out.println("Choose the seats");
					String choosedseats=input.nextLine();
					String Expression="^[0-9]{1,2}$";
					try {
						int ValueChecker=Integer.parseInt(choosedseats);
						if(seats.contains(choosedseats) && choosedseats.matches(Expression) && ValueChecker<=30) {
							Chosenseats.add(choosedseats);
							seats.set(seats.indexOf(choosedseats), "X");
							TotalCount-=1;
						}
						else if(Chosenseats.contains(choosedseats)) {
							System.out.println("seat has already been chosen. Please try Again with Some Other seats.");
							continue;
						}
						else {
							System.out.println("The choosen seat is not availabale!!!");
							continue;
						}
					}
					catch(Exception ex) {
						System.out.println("Invalid Input");
						log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
						continue;
					}
				}
			break;
			}
			///////////////////////////////////////////////
			TotalCount=Adults+Children;
			flight=flight.selectFlight(FlightNumber,"Round Trip",user.getPassengerId(),new Trip(trip.getLoggedUser(),trip.getTo(),trip.getFrom(),trip.getReturnDate(),null,"One Way",economy));//choosed flight	
			List<String> newseats=new ArrayList<String>();
			List<String> newChosenseats=new ArrayList<String>();
			newseats.addAll(flight.getAvailableSeats());
//			System.out.println(seats);
			
		while(true) {
			while(TotalCount>0) {
				int index=0;
				
				for(int i=0;i<5;i++) {
					for(int j=0;j<6;j++) {
						System.out.print(newseats.get(index)+"\t");//index out of bound
						index++;
					}
					System.out.println();
					System.out.println();
				}
				System.out.println("Choose the seats for return flight");
				String choosedseats=input.nextLine();
				String Expression="^[0-9]{1,2}$";
				try {
					int ValueChecker=Integer.parseInt(choosedseats);
					if(newseats.contains(choosedseats) && choosedseats.matches(Expression) && ValueChecker<=30) {
						newChosenseats.add(choosedseats);
						newseats.set(newseats.indexOf(choosedseats), "X");
						TotalCount-=1;
					}
					else if(newChosenseats.contains(choosedseats)) {
						System.out.println("seat has already been chosen. Please try Again with Some Other seats.");
						continue;
					}
					else {
						System.out.println("The choosen seat is not availabale!!!");
						continue;
					}
				}
				catch(Exception ex) {
					System.out.println("Invalid Input");
					log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
					continue;
				}
			}
		break;
		}
			//////////////////////////////////////////////
			
//			System.out.println(seats);
//			System.out.println(Chosenseats);
			flight.setBookedFlightDetails(flight,trip, seats);
			flight.updateBookedSeats(seats,flight,trip);
			seat=new Seats(trip.getLoggedUser(),flight.getFlightName(),Adults,Children,TotalCount,Chosenseats);
			seat.setSeats(seat);
			Tickets ticket=new Tickets(user, flight, trip, seat, Chosenseats, newChosenseats,economyPrice);
			ticket.SetTicket();
			ticket.showTickets();

	}

	//this method check whether the name and password is matching with the existing user if it have it successfully logged in
	
	public boolean ValidateSignIn(String Name,String Password) {
		Connection con=DBConnection.getDBConnection();
		try {
			Statement smt=con.createStatement();
			ResultSet rs=smt.executeQuery("select * from User");
			while(rs.next()) {
				if(rs.getString(1).equals(Name) && rs.getString(2).equals(Password)){
					User=rs.getString(5);
					user=new Passenger(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+e);
		}
		return true;
	}

	
	
	
	// this method get the verified detail of user and check the data base did the user already exit if not it return true that is the new user get created
	public boolean ValidateSignUp(String Name,String Password,String number,String id) {
		User="User";
		Connection con=DBConnection.getDBConnection();
		try {
			Statement smt=con.createStatement();
			ResultSet rs=smt.executeQuery("select * from User");
			while(rs.next()) {
				if(rs.getString(1).equals(Name) || (rs.getString(2).equals(Password) && rs.getString(3).equals(number) && rs.getString(4).equals(id))) {
					
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+e);
			System.out.println(e.getMessage());
		}
		return true;
		
	}
	//Validate the name
	public boolean validateName(String Name) {
		String NameExpression="^[a-zA-Z\s]+$";
		if(Name.matches(NameExpression)) {
			return true;
		}
		return false;
	}
	//validate the mobile number
	public boolean validateNumber(String Number) {
		String NumberExpression="\\d{10}";
		if(Number.matches(NumberExpression)) {
			return true;
		}
		return false;
	}
	//validate password
	public boolean validatePassword(String PassWord) {
		String passWordExpression="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-,._/\\`~;:'\"]).{8,}$";
		if(PassWord.matches(passWordExpression)) {
			return true;
		}
		return false;
	}
	 public void Update(String str) {
		 Scanner sc=new Scanner(System.in);
			String time1,time2,time3,time4;
			String regexPatter="^([0-1]?[0-9]|[2][0-3]):([0-5][0-9])(:[0-5][0-9])?$";
			if(str.equals("One Way")) {
				new Flight().setFlightInfo("One Way");
				int FlightNumber=0;
				System.out.println("Select the flight with Flight Number");
				while(true) {
					try {
						FlightNumber=sc.nextInt();
						sc.nextLine();
						if(FlightNumber>0 && FlightNumber<6) {
							break;
						}
						System.out.println("Flight number 1-5");
						}
					catch(Exception ex) {
						System.out.println("Error in getting flightNumber in One Way");
						log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
					}
				}
				while(true) {
					System.out.println("Enter the from Time(HH:MM:SS)");
					 time1=sc.nextLine();
					 if(time1.matches(regexPatter)) {
						 break;
					 }
				}
				while(true) {
					System.out.println("Enter the to Time(HH:MM:SS)");
					 time2=sc.nextLine();
					 if(time2.matches(regexPatter)) {
						 break;
					 }
				}
				
				UpdateTime(time1,time2,null,null,FlightNumber);
			}
			else {
				new Flight().setFlightInfo("Round Trip");
				int FlightNumber=0;
				System.out.println("Select the flight with Flight Number");
				while(true) {
					try {
						FlightNumber=sc.nextInt();
						sc.nextLine();
						if(FlightNumber>0 && FlightNumber<6) {
							break;
						}
						System.out.println("Flight number 1-5");
						}
					catch(Exception ex) {
						System.out.println("Error in getting flightNumber in One Way");
						log.log(Level.ERROR,user.getPassengerId()+" "+LocalDateTime.now()+" "+ex);
					}
				}
				while(true) {
					System.out.println("Enter the from Time(HH:MM:SS)");
					 time1=sc.nextLine();
					 if(time1.matches(regexPatter)) {
						 break;
					 }
				}
				while(true) {
					System.out.println("Enter the to Time(HH:MM:SS)");
					 time2=sc.nextLine();
					 if(time2.matches(regexPatter)) {
						 break;
					 }
				}
				while(true) {
					System.out.println("Enter the Return from Time(HH:MM:SS)");
					 time3=sc.nextLine();
					 if(time3.matches(regexPatter)) {
						 break;
					 }
				}
				while(true) {
					System.out.println("Enter the Return To Time(HH:MM:SS)");
					time4=sc.nextLine();
					 if(time4.matches(regexPatter)) {
						 break;
					 }
			}
				
				UpdateTime(time1,time2,time3,time4,FlightNumber);
			}
		
	 }
	 public void UpdateTime(String t1,String t2,String t3,String t4,int FlightNumber) {
		 try {
				Connection con=DBConnection.getDBConnection();
				if(t3==null) {
					PreparedStatement psmt=con.prepareStatement("Update FlightDetails set FromTime=?,ToTime=?,ReturnFromTime=?,ReturnToTime=? where FlightNumber=? and ReturnFromTime IS NULL");
					psmt.setTime(1, Time.valueOf(t1));
					psmt.setTime(2, Time.valueOf(t2));
					psmt.setNull(3, Types.TIME);
					psmt.setNull(4, Types.TIME);
					psmt.setInt(5, FlightNumber);
					psmt.executeUpdate();
					ResultSet rs=psmt.executeQuery("Select * from FlightDetails");
					new Flight().setFlightInfo("One Way");
				}
				else {
					PreparedStatement psmt=con.prepareStatement("Update FlightDetails set FromTime=?,ToTime=?,ReturnFromTime=?,ReturnToTime=? where FlightNumber=? and ReturnFromTime IS NOT NULL");
					psmt.setTime(1, Time.valueOf(t1));
					psmt.setTime(2, Time.valueOf(t2));
					psmt.setTime(2, Time.valueOf(t3));
					psmt.setTime(2, Time.valueOf(t4));
					psmt.setInt(5, FlightNumber);
					psmt.executeUpdate();
					ResultSet rs=psmt.executeQuery("Select * from FlightDetails");
					new Flight().setFlightInfo("Round Trip");
				}

			} catch (Exception e) {
						e.printStackTrace();
			}
	 }
	

}




//luggage
//after changing the time insist the user to the flight at that time get delied
//multicity
//add flight











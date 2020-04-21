package main;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
//import java.io.Console;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import persistence.DataSource;
import types.Customer;
import types.Reservation;
import types.Room;

@SuppressWarnings("unused")
public class Main {
	
	private static DataSource dataSource;
	private static final Scanner sc = new Scanner(System.in);
//	private static final Console cons = System.console();
	private static Customer cust;
	private static List<Room> rooms;
	
	private static ResourceBundle resource = ResourceBundle.getBundle("messages");

	public static void main(String[] args) {
		
		dataSource = loadDataSource();
		
		clearScreen(0);
		loginMenu();
		
		boolean hasExited = false;
        
        while (!hasExited && cust != null) {
            
            clearScreen(0);
            System.out.print(resource.getString("mainMenu"));
            //System.out.print("Please select an option\n1 - Reservations\n2 - Manage account\n0 - Exit\n>>>");
            if (sc.hasNextInt()) {
                switch (Integer.parseInt(sc.nextLine())) {
                    case 0:
                        System.out.println("Goodbye!");
                        hasExited = true;
                        break;
                    case 1:
                        reservationMenu();
                        break;
                    case 2:
                        clearScreen(0);
                        accountMenu();
                        break;
                    default:
                }
            }
            else {
                sc.nextLine();
            }
        }
		System.out.println(cust);
	}
	
	public static void accountMenu() {
        
        boolean hasExited = false;
        while (!hasExited) {
            clearScreen(0);
            System.out.print(resource.getString("accountMenu"));
            if (sc.hasNextInt()) {
                switch (Integer.parseInt(sc.nextLine())) {
                    case 0:
                        hasExited = true;
                        clearScreen(0);
                        break;
                    case 1:
                        clearScreen(0);
                        updateCustomer();
                        break;
                    case 2:
                        clearScreen(0);
                        updatePassword();
                        clearScreen(3);
                        break;
                    case 3:
                        clearScreen(0);
                        dataSource.deleteCustomer(cust);
                        cust = null;
                        hasExited = true;
                        break;
                    default:
                }
            }
            else {
                sc.nextLine();
            }
        }
	}
	
	public static void reservationMenu() {
	    //List<Reservation> reservations = dataSource.fetchReservations(cust);
	    
	    boolean hasExited = false;
	    while (!hasExited) {
	        
	        clearScreen(0);
	        System.out.print(resource.getString("reservationMenu"));
            if (sc.hasNextInt()) {
                switch (Integer.parseInt(sc.nextLine())) {
                    case 0:
                        hasExited = true;
                        clearScreen(3);
                        break;
                    case 1:
                        createReservation();
                        clearScreen(3);
                        break;
                    case 2:
                        deleteReservation();
                        break;
                    case 3:
                        printReservations(cust);
                        break;
                    default:
                }
            }
            else {
                sc.nextLine();
            }
        }
	}
	
	public static Reservation createReservation() {
	    LocalDate start = null, end = null;
	    boolean isValid = false;
	    
	    while (!isValid) {

            System.out.print(resource.getString("startDate"));
            String startInput = sc.nextLine();
            System.out.print(resource.getString("endDate"));
            String endInput = sc.nextLine();
            
            try {
                start = LocalDate.parse(startInput);
                end = LocalDate.parse(endInput);
                
                if (start.isAfter(LocalDate.now()) && end.isAfter(start)) {
                    isValid = true;
                }
            } catch (DateTimeParseException e) {}
	    }
        
	    return dataSource.saveReservation(cust.getId(), start, end);
	}
	
	public static boolean deleteReservation() {
	    return true;
	}
	
	public static void printReservations(Customer cust) {
	    List<Reservation> reservations = dataSource.fetchReservations(cust);
	    reservations.forEach(System.out::println);
	}
	
	
	public static boolean updatePassword() {
	    boolean updateSucceeded = false;

	    System.out.print(resource.getString("oldPassword"));
        String oldPassword = sc.nextLine();
        
        if (validateCustomer(cust.getEmail(), oldPassword) ) {
        
            System.out.print(resource.getString("newPassword"));
            String newPassword = sc.nextLine();
            
            updateSucceeded = dataSource.updateCustomerPassword(cust, newPassword);
        } 
        else {
            System.out.print(resource.getString("wrongPassword"));
        }
        
        return updateSucceeded;
    }
	
//	public static Customer getCustomerInfo() {
//	    System.out.print(resource.getString("details"));
//        System.out.print(resource.getString("firstName"));
//	    String firstName = sc.nextLine();
//	    
//        System.out.print(resource.getString("lastName"));
//	    String lastName = sc.nextLine();
//	    
//        System.out.print(resource.getString("email"));
//	    String email = sc.nextLine();
//	    
//        System.out.print(resource.getString("phone"));
//	    String phone = sc.nextLine();
//        phone = phone.equals("") ? null : phone;  
//        
//        return new Customer(firstName, lastName, email, phone); 
//    }

	public static Customer createCustomer() {

      System.out.print(resource.getString("details"));

      System.out.print(resource.getString("firstName"));
      String firstName = sc.nextLine();
      
      System.out.print(resource.getString("lastName"));
      String lastName = sc.nextLine();
      
      System.out.print(resource.getString("email"));
      String email = sc.nextLine();
      
      System.out.print(resource.getString("phone"));
      String phone = sc.nextLine();
      phone = phone.equals("") ? null : phone;
      
      String password = getCustomerPassword();
      
      return dataSource.addCustomer(firstName, lastName, email, phone, password);
  }
    
    public static String getCustomerPassword() {
        String password = null, confirmed = null;
        
        boolean passwordIsValid = false;

        while (!passwordIsValid) {
            System.out.print(resource.getString("newPassword"));
            password = sc.nextLine();
            System.out.print(resource.getString("confirmPassword"));
            confirmed = sc.nextLine();
  
            if (!password.equals(confirmed)) {
                System.out.println(resource.getString("passwordMismatch"));
            }
            else if (password.length() < 3) {
                System.out.println(resource.getString("shortPassword"));
            }
            else {
                passwordIsValid = true;
            }
        }
        return password;
    }
	
	public static Customer loginCustomer() {
        System.out.print(resource.getString("email"));
	    String email = sc.nextLine();
        System.out.print(resource.getString("password"));
	    String password = sc.nextLine();
        
        if (validateCustomer(email, password)) {
            System.out.print(resource.getString("successfulLogin"));
            return dataSource.fetchCustomer(email);
        }
        else {
            System.out.print(resource.getString("unsuccessfulLogin"));
            return null;
        }
	}
	
	public static boolean updateCustomer() {
	    
	    System.out.print(resource.getString("details"));

        System.out.print(resource.getString("firstName"));
        cust.setFirstName( sc.nextLine() );
          
        System.out.print(resource.getString("lastName"));
        cust.setLastName( sc.nextLine() );
          
        System.out.print(resource.getString("email"));
        cust.setEmail( sc.nextLine() );
          
        System.out.print(resource.getString("phone"));
        String phone = sc.nextLine();
        cust.setPhone(phone.equals("") ? null : phone);
        
        return dataSource.updateCustomer(cust);
    }
	
	public static void loginMenu() {
	    boolean hasExited = false;
	    
	    while (!hasExited && cust == null) {
	        System.out.print(resource.getString("loginMenu"));
	        if (sc.hasNextInt()) {
	            switch (Integer.parseInt(sc.nextLine())) {
	                case 0:
	                    System.out.println("Goodbye!");
	                    hasExited = true;
	                    break;
	                case 1:
	                    cust = loginCustomer();
	                    clearScreen(3);
	                    break;
	                case 2:
	                    cust = createCustomer();
	                    break;
	                default:
	            }
	        }
	        else {
	            sc.nextLine();
	        }
	    }
	}
	
	public static boolean validateCustomer(String email, String password) {
        return (password.equals(dataSource.fetchPassword(email)) ) ? true : false;
    }

	public static DataSource loadDataSource() {
        return ServiceLoader.load(DataSource.class).findFirst()
                .orElseThrow(() -> new NoSuchElementException(resource.getString("noDatasourceErr")));
    }
	
	public static void clearScreen(int secondsDelay) {
	    
	    try {
            TimeUnit.SECONDS.sleep(secondsDelay);
        } catch (InterruptedException ie) {}
	    
	    System.out.print("\033[H\033[2J");
	    System.out.flush();
	}
}

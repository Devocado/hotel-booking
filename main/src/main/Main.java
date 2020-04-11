package main;

//import java.io.Console;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

import persistence.DataSource;
import types.Customer;
import types.Reservation;
import types.Room;

public class Main {
	
	private static DataSource dataSource;
	private static final Scanner sc = new Scanner(System.in);
//	private static final Console cons = System.console();
	private static Customer cust;
	private static List<Room> rooms;
	
	private static ResourceBundle resource = ResourceBundle.getBundle("messages");

	public static void main(String[] args) {
		
		dataSource = loadDataSource();
		rooms = dataSource.getRooms();
//		clearScreen(0);
//		loginMenu();
//		
//		boolean hasExited = false;
//        
//        while (!hasExited && cust != null) {
//            
//            clearScreen(0);
//            System.out.print(resource.getString("mainMenu"));
//            //System.out.print("Please select an option\n1 - Reservations\n2 - Manage account\n0 - Exit\n>>>");
//            if (sc.hasNextInt()) {
//                switch (Integer.parseInt(sc.nextLine())) {
//                    case 0:
//                        System.out.println("Goodbye!");
//                        hasExited = true;
//                        break;
//                    case 1:
//                        System.out.println("Sorry, that has not been implemented yet");
//                        clearScreen(2);
//                        break;
//                    case 2:
//                        clearScreen(0);
//                        accountMenu();
//                        break;
//                    default:
//                }
//            }
//            else {
//                sc.nextLine();
//            }
//        }
//		System.out.println(cust);
	}
	
	public static void accountMenu() {
        
        boolean hasExited = false;
        while (!hasExited) {
            clearScreen(0);
            System.out.print(resource.getString("accountMenu"));
//            System.out.print("Please select an option\n1 - Update details\n2 - Change password\n"
//                    + "3 - Delete account\n0 - Exit\n>>>");
            if (sc.hasNextInt()) {
                switch (Integer.parseInt(sc.nextLine())) {
                    case 0:
                        hasExited = true;
                        clearScreen(0);
                        break;
                    case 1:
                        clearScreen(0);
                        cust = updateCustomer();
                        break;
                    case 2:
                        clearScreen(0);
//                        System.out.println(updatePassword());
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
	    List<Reservation> reservations = dataSource.fetchReservations(cust);
	    
	    boolean hasExited = false;
	    while (!hasExited && cust == null) {
	        
	        clearScreen(0);
	        System.out.print(resource.getString("reservationMenu"));
//            System.out.print("Please select an option\n1 - Make a reservation\n"
//                    + "2 - Remove a reservation\n3 - View reservations\n0 - Exit\n>>>");
            if (sc.hasNextInt()) {
                switch (Integer.parseInt(sc.nextLine())) {
                    case 0:
                        hasExited = true;
                        clearScreen(3);
                        break;
                    case 1:
                        cust = loginCustomer();
                        clearScreen(3);
                        break;
                    case 2:
                        //cust = createCustomer();
                        break;
                    case 3:
                        reservations.forEach(System.out::println);
                        break;
                    default:
                }
            }
            else {
                sc.nextLine();
            }
        }
	}
	
	public static boolean updatePassword() {
	    boolean updateSucceeded = false;
	    
//	    System.out.print("Please enter your current password: \n>>>");
	    System.out.print(resource.getString("oldPassword"));
        String oldPassword = sc.nextLine();
        
        if (validateCustomer(cust.getEmail(), oldPassword) ) {
        
//            System.out.print("Please enter your new password: \n>>>");
            System.out.print(resource.getString("newPassword"));
            String newPassword = sc.nextLine();
            
            updateSucceeded = dataSource.updateCustomerPassword(cust, newPassword);
        } 
        else {
            System.out.print(resource.getString("wrongPassword"));
//            System.out.println("You entered your current password incorrectly");
        }
        
        return updateSucceeded;
    }
	
	public static Customer getCustomerInfo() {
//        System.out.println("Please enter your details:");
	    System.out.print(resource.getString("details"));
//        System.out.print("First name: >>>");
        System.out.print(resource.getString("firstName"));
	    String firstName = sc.nextLine();
	    
//        System.out.print("Last name: >>>");
        System.out.print(resource.getString("lastName"));
	    String lastName = sc.nextLine();
	    
//        System.out.print("Email address: >>>");
        System.out.print(resource.getString("email"));
	    String email = sc.nextLine();
	    
//        System.out.print("Phone number, or hit enter: >>>");
        System.out.print(resource.getString("phone"));
	    String phone = sc.nextLine();
        phone = phone.equals("") ? null : phone;  
        
        return new Customer(firstName, lastName, email, phone); 
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
//	    System.out.print("Please enter your email address:\n >>>");
        System.out.print(resource.getString("email"));
	    String email = sc.nextLine();
//        System.out.print("Please enter your account password:\n >>>");
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
	
	public static Customer updateCustomer() {
        Customer updatedCust = getCustomerInfo();
        dataSource.updateCustomer(cust, updatedCust);
        return updatedCust;
    }
	
	public static void loginMenu() {
	    boolean hasExited = false;
	    
	    while (!hasExited && cust == null) {
	        System.out.print(resource.getString("loginMenu"));
//	        System.out.print("Please select an option\n1 - Login\n2 - Sign up\n0 - Exit\n>>>");
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
	                    cust = getCustomerInfo();
	                    dataSource.addCustomer(cust, getCustomerPassword());
	                    clearScreen(0);
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

package main;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ServiceLoader;

import persistence.DataSource;
import types.Customer;
import types.Reservation;

public class Main {
	
	private static DataSource dataSource;
	private static final Scanner sc = new Scanner(System.in);
	private static Customer cust;

	public static void main(String[] args) {
		
		dataSource = loadDataSource();
		
		loginMenu();
		
		boolean hasExited = false;
        
        while (!hasExited && cust != null) {
            System.out.print("Please select an option\n1 - Reservations\n2 - Manage account\n0 - Exit\n>>>");
            if (sc.hasNextInt()) {
                switch (Integer.parseInt(sc.nextLine())) {
                    case 0:
                        System.out.println("Goodbye!");
                        hasExited = true;
                        break;
                    case 1:
                        System.out.println("Sorry, that has not been implemented yet");
                        break;
                    case 2:
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
            System.out.print("Please select an option\n1 - Update details\n2 - Change password\n"
                    + "3 - Delete account\n0 - Exit\n>>>");
            if (sc.hasNextInt()) {
                switch (Integer.parseInt(sc.nextLine())) {
                    case 0:
                        hasExited = true;
                        break;
                    case 1:
                        cust = updateCustomer();
                        break;
                    case 2:
                        System.out.println(updatePassword());
                        break;
                    case 3:
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
            System.out.print("Please select an option\n1 - Make a reservation\n"
                    + "2 - Remove a reservation\n3 - View reservations\n0 - Exit\n>>>");
            if (sc.hasNextInt()) {
                switch (Integer.parseInt(sc.nextLine())) {
                    case 0:
                        hasExited = true;
                        break;
                    case 1:
                        cust = loginCustomer();
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
	    
	    System.out.print("Please enter your current password: \n>>>");
        String oldPassword = sc.nextLine();
        System.out.print("Please enter your new password: \n>>>");
        String newPassword = sc.nextLine();
        
        if (validateCustomer(cust.getEmail(), oldPassword) ) {
            updateSucceeded = dataSource.updateCustomerPassword(cust, newPassword);
        }
        return updateSucceeded;
    }
	
	public static Customer getCustomerInfo() {
        System.out.println("Please enter your details:");
        System.out.print("First name: >>>");
        String firstName = sc.nextLine();
        System.out.print("Last name: >>>");
        String lastName = sc.nextLine();
        System.out.print("Email address: >>>");
        String email = sc.nextLine();
        System.out.print("Phone number, or hit enter: >>>");
        String phone = sc.nextLine();
        phone = phone.equals("") ? null : phone;  
        
        return new Customer(firstName, lastName, email, phone); 
    }
    
    public static String getCustomerPassword() {
        System.out.print("Please enter a password for your account: >>>");
        String password = sc.nextLine();
        
        return password;
    }
	
	public static Customer loginCustomer() {
	    System.out.print("Please enter your email address: \n>>>");
        String email = sc.nextLine();
        System.out.print("Please enter your account password: \n>>>");
        String password = sc.nextLine();
        
        if (validateCustomer(email, password)) {
            System.out.println("Logged in successfully");
            return dataSource.fetchCustomer(email);
        }
        else {
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
	        System.out.print("Please select an option\n1 - Login\n2 - Sign up\n0 - Exit\n>>>");
	        if (sc.hasNextInt()) {
	            switch (Integer.parseInt(sc.nextLine())) {
	                case 0:
	                    System.out.println("Goodbye!");
	                    hasExited = true;
	                    break;
	                case 1:
	                    cust = loginCustomer();
	                    break;
	                case 2:
	                    cust = getCustomerInfo();
	                    dataSource.addCustomer(cust, getCustomerPassword());
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
                .orElseThrow(() -> new NoSuchElementException("No datasource service provider found"));
    }
}

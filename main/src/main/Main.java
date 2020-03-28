package main;

import java.util.ServiceLoader;

import persistence.DataSource;
import types.Customer;

public class Main {
	
	private static DataSource dataSource;

	public static void main(String[] args) {
		
		System.out.println("Project is running!");
		dataSource = loadDataSource();
		
		Customer cust = dataSource.fetchCustomer("henryagen@hotmail.com");
		
		if (cust == null) {
		    cust = new Customer("Harry", "Lampton", "hlampton@starmail.uk");
		    dataSource.addCustomer(cust, "Secretpassword");
		}
		
		//System.out.println(dataSource.deleteCustomer(cust));
		
		System.out.println(cust);

	}
	
	public static DataSource loadDataSource() {
		return ServiceLoader.load(DataSource.class).findFirst().orElseThrow();
	}
	
	
}

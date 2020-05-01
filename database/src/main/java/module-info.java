module database {
	requires java.sql;
	requires mysql.connector.java;
	requires transitive types;
	
	exports database;
}
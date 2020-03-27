module database {
	requires main;
	requires java.sql;
	requires mysql.connector.java;
	provides persistence.DataSource with database.DataBase;
}
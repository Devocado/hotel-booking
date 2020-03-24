module database {
	requires main;
	requires java.sql;
	requires mysql.connector.java;
	provides types.DataSource with database.DataBase;
}
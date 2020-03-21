module database {
	requires main;
	provides types.DataSource with database.DataBase;
}
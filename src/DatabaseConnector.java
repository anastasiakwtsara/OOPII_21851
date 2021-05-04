

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class DatabaseConnector {
	private  Connection db_con_obj = null; //A connection (session) with a specific database. SQL statements are executed and results are returned within the context 
	//of a connection. A Connection object's database is able to provide information describing its tables, its supported SQL grammar, its stored procedures, 
	//the capabilities of this connection, and so on. This information is obtained with the getMetaData method.
	private  PreparedStatement db_prep_obj = null;//An object that represents a precompiled SQL statement.
	//A SQL statement is precompiled and stored in a PreparedStatement object. This object can then be used to efficiently execute this statement multiple times.
	

	public  void makeJDBCConnection() {
		 
		try {//We check that the DB Driver is available in our project.		
			Class.forName("oracle.jdbc.driver.OracleDriver"); //This code line is to check that JDBC driver is available. Or else it will throw an exception. Check it with 2. 
			System.out.println("Congrats - Seems your oracle JDBC Driver Registered!"); 
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			e.printStackTrace();
			return;
		}
 
		try {
			// DriverManager: The basic service for managing a set of JDBC drivers.	 //We connect to a DBMS.
			db_con_obj = DriverManager.getConnection("jdbc:oracle:thin:@oracle12c.hua.gr:1521:orcl","it21851","it21851");// Returns a connection to the URL.
			//Attempts to establish a connection to the given database URL. The DriverManager attempts to select an appropriate driver from the set of registered JDBC drivers.
			if (db_con_obj != null) { 
				System.out.println("Connection Successful! Enjoy. Now it's time to CRUD data. ");
				
			} else {
				System.out.println("Failed to make connection!");
			}
		} catch (SQLException e) {
			System.out.println("Oracle Connection Failed!");
			e.printStackTrace();
			return;
		}
 
	}
	public  Map<String,City> ReadCities() throws SQLException {
	db_prep_obj = db_con_obj.prepareStatement("select * from cities");
	ResultSet  rs = db_prep_obj.executeQuery();
	
	Map<String, City> cities = new HashMap<>();

    while (rs.next()){
    	City city = new City();
    	city.setName(rs.getString("cityname"));
    	city.setCountry(rs.getString("code"));
    	double[] geodesicVector = new double[2];
    	geodesicVector[0]=rs.getDouble("latitude");
    	geodesicVector[1]=rs.getDouble("longtitude");
        city.setGeodesicVector(geodesicVector);
        int[] termsVector = new int[10];
        termsVector[0]=rs.getInt("term1");
        termsVector[1]= rs.getInt("term2");
        termsVector[2]= rs.getInt("term3");
        termsVector[3]= rs.getInt("term4");
        termsVector[4]= rs.getInt("term5");
        termsVector[5]= rs.getInt("term6");
        termsVector[6]= rs.getInt("term7");
        termsVector[7]= rs.getInt("term8");
        termsVector[8] = rs.getInt("term9");
        termsVector[9]= rs.getInt("term10");
        city.setTermsVector(termsVector);

        cities.put(rs.getString("cityname"),city);
        
        
    }
    return cities;
}
	
	public void addDataToDB(String cityname, String code, double latitude, double longtitude, int term_1, int term_2, int term_3, int term_4, int term_5, int term_6, 
			int term_7, int term_8, int term_9, int term_10) {
		 
		try {
			String insertQueryStatement = "INSERT  INTO  CITIES  VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";			
			db_prep_obj = db_con_obj.prepareStatement(insertQueryStatement);
			db_prep_obj.setString(1, cityname);//.setInt(1, newKey);//.setString
			db_prep_obj.setString(2, code);
			db_prep_obj.setDouble(3, latitude);//.setInt(2, year);
			db_prep_obj.setDouble(4, longtitude);
			db_prep_obj.setInt(5, term_1);
			db_prep_obj.setInt(6, term_2);
			db_prep_obj.setInt(7, term_3);
			db_prep_obj.setInt(8, term_4);
			db_prep_obj.setInt(9, term_5);
			db_prep_obj.setInt(10, term_6);
			db_prep_obj.setInt(11, term_7);
			db_prep_obj.setInt(12, term_8);
			db_prep_obj.setInt(13, term_9);
			db_prep_obj.setInt(14, term_10);			
			// execute insert SQL statement Executes the SQL statement in this PreparedStatement object, which must be an SQL Data Manipulation Language (DML) statement
			int numRowChanged = db_prep_obj.executeUpdate(); //either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
			System.out.println("Rows "+numRowChanged+" changed.");
			
		} catch (
 
		SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
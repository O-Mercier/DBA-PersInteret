package ca.qc.cvm.dba.persinteret.dao;

import java.io.File;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class BerkeleyConnection {
	private static Database connection;
	private static Environment environment;
	private static final String DB_PATH = "database/";
	private static final String DB_NAME = "personne_db";
	
	/**
	 * Méthode qui permet de retourner une connexion à la base de données
	 * 
	 * @return
	 */
	public static Database getConnection() {
		if (connection == null) {
		 
	        try {
	        	File dbPath = new File(DB_PATH);
	        	
	        	if (!dbPath.isDirectory()) {
	        		dbPath.mkdir();
	        	}
	        	
	            // Open the environment, creating one if it does not exist
	            EnvironmentConfig envConfig = new EnvironmentConfig();
	            envConfig.setAllowCreate(true);
	            envConfig.setTransactional(true);
	            environment = new Environment(new File(DB_PATH), envConfig);
	 
	            // Open the database, creating one if it does not exist
	            DatabaseConfig dbConfig = new DatabaseConfig();
	            dbConfig.setTransactional(true);
	            dbConfig.setAllowCreate(true);
	            
	            connection = environment.openDatabase(null, DB_NAME, dbConfig);
	        } 
	        catch (Exception dbe) {
	            dbe.printStackTrace();
	        }
		}
		
		return connection;
	}
	
	/**
	 * Méthode permettant de tester la connexion
	 * 
	 * @return si la connexion est ouverte ou pas
	 */
	public static boolean connectionCredentialsValid() {
		Database c = getConnection();
		boolean valid = c != null;
		releaseConnection();
		
		return valid;
	}
	
	public static void releaseConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
				environment.close();
				environment = null;
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

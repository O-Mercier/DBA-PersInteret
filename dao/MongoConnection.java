package ca.qc.cvm.dba.persinteret.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
	private static MongoClient mongoClient; 
	private static MongoDatabase connection;
	private static final String DB_NAME = "personne_db";
	private static final String DB_HOST = "localhost";
	
	/**
	 * Méthode qui permet de retourner une connexion à la base de données
	 * 
	 * @return
	 */
	public static MongoDatabase getConnection() {
		if (connection == null) {
			
			try {
				Logger mongoLogger = Logger.getLogger( "org.mongodb" );
				mongoLogger.setLevel(Level.WARNING);
				
				mongoClient = new MongoClient(DB_HOST);
				connection = mongoClient.getDatabase(DB_NAME);
				
			} 
			catch (Exception e) {
				e.printStackTrace();
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
		MongoDatabase c = getConnection();
		boolean valid = c != null;
		releaseConnection();
		
		return valid;
	}
	
	public static void releaseConnection() {
		if (connection != null) {
			try {
				mongoClient.close();
				connection = null;
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

package ca.qc.cvm.dba.persinteret.dao;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;

import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.TextSearchOptions;

import ca.qc.cvm.dba.persinteret.entity.Person;

public class PersonDAO {
	
	

	/**
	 * Méthode permettant de retourner la liste des personnes de la base de données.
	 * 
	 * Notes importantes:
	 * - N'oubliez pas de limiter les résultats en fonction du paramètre limit
	 * - La liste doit être triées en ordre croissant, selon les noms des personnes
	 * - Le champ de filtre doit permettre de filtrer selon le préfixe du nom (insensible à la casse)
	 * - Si le champ withImage est à false, alors l'image (byte[]) n'a pas à faire partie des résultats
	 * - N'oubliez pas de mettre l'ID dans la personne, car c'est utile pour savePerson()
	 * - Il pourrait ne pas y avoir de filtre (champ filtre vide)
	 * 
	 * @param filterText champ filtre, peut être vide ou null
	 * @param withImage true si l'image est nécessaire, false sinon.
	 * @param limit permet de restreindre les résultats
	 * @return la liste des personnes, selon le filtre si nécessaire et filtre
	 */
	public static List<Person> getPeopleList(String filterText, boolean withImage, int limit) {
		final List<Person> peopleList = new ArrayList<Person>();
		MongoDatabase connection = MongoConnection.getConnection();
		MongoCollection<Document> collection = connection.getCollection("personnes");
		collection.createIndex(Indexes.ascending("name"));
		/*
		//fuuuuuck me
		Document filter = new Document("")
		FindIterable<Document> result;
		if(filterText != null) {
			Bson filter = Filters.regex("name",".*" + filterText + ".*");
			result = collection.find(filter).limit(limit);
		}
		else
			return =*/

		return peopleList;
	}

	/**
	 * Méthode permettant de sauvegarder une personne
	 * 
	 * Notes importantes:
	 * - Si le champ "id" n'est pas null, alors c'est une mise à jour, pas une insertion
	 * - Le nom de code est optionnel, le reste est obligatoire
	 * - Le nom de la personne doit être unique
	 * - Regarder comment est fait la classe Personne pour avoir une idée des données à sauvegarder
	 * - Pour cette méthode, dénormaliser pourrait vous être utile. La performance des lectures est vitale.
	 * - Je vous conseille de sauvegarder la date de naissance en format long (en millisecondes)
	 * - Une connexion va dans les deux sens. Plus préçisément, une personne X qui connait une autre personne Y
	 *   signifie que cette dernière connait la personne X
	 * - Attention, les connexions d'une personne pourraient changer avec le temps (ajout/suppression)
	 * - À l'insertion, il doit y avoir un id numérique unique associé à la personne. 
	 *   Dépendemment de la base de données, vous devrez trouver une stratégie pour faire un id numérique.
	 * 
	 * @param person
	 * @return true si succès, false sinon
	 */
	public static boolean save(Person person) {
		boolean success = false;
		try {
			MongoDatabase connection = MongoConnection.getConnection();
			MongoCollection<Document> collection = connection.getCollection("personnes");
			Document doc = new Document ();
			doc.append("id", person.getId());
			doc.append("name", person.getName());
			doc.append("codeName", person.getCodeName());
			doc.append("dateOfBirth", person.getDateOfBirth());
			doc.append("status", person.getStatus());
			doc.append("imageData", person.getImageData());
			collection.insertOne(doc);
			success = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return success;
	}
	
	/**
	 * Suppression des données/fiche d'une personne
	 * 
	 * @param person
	 * @return true si succès, false sinon
	 */
	public static boolean delete(Person person) {
		boolean success = true;
		
		return success;
	}
	
	/**
	 * Suppression totale de toutes les données du système!
	 * 
	 * @return true si succès, false sinon
	 */
	public static boolean deleteAll() {
		boolean success = true;

		return success;
	}
	
	/**
	 * Méthode qui retourne le ratio de personnes en liberté par rapport au nombre total de fiches.
	 * 
	 * @return ratio entre 0 et 100
	 */
	public static int getFreeRatio() {
		int num = 0;
		
		return num;
	}
	
	/**
	 * Nombre de photos actuellement sauvegardées dans le système
	 * @return nombre
	 */
	public static long getPhotoCount() {
		return 0;
	}
	
	/**
	 * Nombre de fiches présente dans la base de données
	 * @return nombre
	 */
	public static long getPeopleCount() {
		MongoDatabase connection = MongoConnection.getConnection();
		MongoCollection<Document> collection = connection.getCollection("personnes");
		return collection.count();
	}
		
	/**
	 * Permet de savoir la personne la plus jeune du système
	 * 
	 * @return nom de la personne
	 */
	public static String getYoungestPerson() {
		return "--";
	}
	
	/**
	 * Afin de savoir la prochaine personne à investiguer,
	 * Il faut retourner la personne qui connait, ou est connu, du plus grand nombre de personnes 
	 * disparues ou décédées (morte). Cette personne doit évidemment avoir le statut "Libre"
	 * 
	 * @return nom de la personne
	 */
	public static String getNextTargetName() {
		
		return "--";
	}
	
	/**
	 * Permet de retourner, l'âge moyen des personnes
	 * 
	 * @return par exemple, 20 (si l'âge moyen est 20 années arrondies)
	 */
	public static int getAverageAge() {
		int resultat = 0;
		return resultat;
	}
}

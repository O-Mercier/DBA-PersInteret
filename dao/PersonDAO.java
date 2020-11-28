package ca.qc.cvm.dba.persinteret.dao;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.Bytes;
import com.mongodb.client.*;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.Block;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.TextSearchOptions;
//test
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.*;
import com.mongodb.client.gridfs.model.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.io.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
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
	 *
	 *
	 * ref: https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/gridfs/
	 */
	public static List<Person> getPeopleList(String filterText, boolean withImage, int limit) {
		final List<Person> peopleList = new ArrayList<Person>();
		byte[] img = null;
		try {
			MongoDatabase connection = MongoConnection.getConnection();
			MongoCollection<Document> collection = connection.getCollection("personnes");
			GridFSBucket bucket = GridFSBuckets.create(connection, "images");
			FindIterable<Document> result;
			Document reg = new Document();
			reg.append("$regex", filterText + ".*");
			reg.append("$options", "i");
			Document query = new Document();
			query.append("name", reg);
			result = collection.find(query).limit(limit);
			for (Document r : result) {
				if (withImage) {
					GridFSDownloadStream downloadStream = bucket.openDownloadStream(r.get("id").toString());
					int fileLength = (int) downloadStream.getGridFSFile().getLength();
					img = new byte[fileLength];
					downloadStream.read(img);
				}
				peopleList.add(new Person(
						 r.getString("name"),
						 r.getString("codeName"),
						 r.getString("status"),
						 r.getString("dateOfBirth"),
						null,
						img));
				peopleList.get(peopleList.size() - 1).setId(r.getLong("id"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
	 *
	 * ref: https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/gridfs/
	 * 		https://stackoverflow.com/questions/41223691/how-to-overwrite-image-in-mongodb-gridfs
	 *
	 */
	public static boolean save(Person person) { //todo unfuck id typing
		boolean success = false;
		try {
			MongoDatabase connection = MongoConnection.getConnection();
			MongoCollection<Document> collection = connection.getCollection("personnes");
			GridFSBucket bucket = GridFSBuckets.create(connection, "images");
			boolean create = (person.getId() == null);
			InputStream imgStream = new ByteArrayInputStream(person.getImageData());
			if (create) {
				if (getPeopleCount() == 0)
					person.setId(0);
				else
					person.setId(collection.find().sort(new Document("_id", -1)).first().getLong("id") + 1);
				Document doc = new Document();
				doc.append("id", person.getId());
				doc.append("name", person.getName());
				doc.append("codeName", person.getCodeName());
				doc.append("dateOfBirth", person.getDateOfBirth());
				doc.append("status", person.getStatus());
				collection.insertOne(doc);
			} else { //todo
				collection.updateOne(eq("id", person.getId()), combine(set("name", person.getName()),
						set("codeName", person.getCodeName()),
						set("dateOfBirth", person.getDateOfBirth()),
						set("status", person.getStatus())));
				bucket.delete(getObjId(connection, person.getId().toString()));
			}
			bucket.uploadFromStream(person.getId().toString(), imgStream);
			collection.createIndex(Indexes.ascending("name"), new IndexOptions().unique(true));
			success = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
	//TODO
	/*private Document insertQuerryBuilder(Person person){ //move combine here

	}

	private Bson updateQuerryBuilder(Person person){ //move combine here

	}*/

	public static ObjectId getObjId(MongoDatabase connection, String id) { //todo unfuck id typing (bis)
		Document query = new Document("filename", id);
		MongoCursor<Document> cursor = connection.getCollection("images.files").find(query).iterator();
		Document document = cursor.next();
		ObjectId _id = document.getObjectId("_id");
		return _id;
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

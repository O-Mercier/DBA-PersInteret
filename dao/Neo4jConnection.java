package ca.qc.cvm.dba.persinteret.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.StatementResult;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;

public class Neo4jConnection {
	private static Driver graphDB;
	private static Session session;

	/**
	 * Méthode permettant d'obtenir une connexion
	 * @return une connexion
	 */
	public static Session getConnection() {		
		if (graphDB == null) {
			graphDB = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "AAAaaa111") );
			session = graphDB.session();
		}
		
		return session;
	}
	
	/**
	 * Méthode permettant de parcourir un chemin et de retirer la liste de noeuds/relations de celui-ci
	 * 
	 * Exemple : MATCH chemin=(n)-[r:CONNAIT]->(n2) RETURN chemin
	 * Retournerait une liste du genre [Noeud1, Relation1a2, Noeud2, Relation2a3, Noeud3]
	 * 
	 * @param query La requête Cypher
	 * @param params Les paramètres de la requête
	 * @return Une liste de noeuds et relations.
	 */
	public static List<Object> getPath(String query, Map<String, Object> params) {
		Session session = Neo4jConnection.getConnection();
		List<Object> resultList = new ArrayList<Object>();
		
		try {					
            StatementResult result = session.run( query, params);
            
            if (result.hasNext()) {
            	Record row = result.next();
            	Path p = row.get("path").asPath();
            	
            	Iterator<Node> nodes = p.nodes().iterator();
				Iterator<Relationship> relationships= p.relationships().iterator();
				
				while (nodes.hasNext()) {
					Node node = nodes.next();
		
					resultList.add(node);
					
					if (relationships.hasNext()) {
						resultList.add(relationships.next());
					}
				}		
            }
        }
		catch (Exception e) {
			e.printStackTrace();
		}

		return resultList;
	}
}

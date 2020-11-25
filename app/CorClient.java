package ca.qc.cvm.dba.persinteret.app;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import ca.qc.cvm.dba.correctionserver.lib.BaseCorClient;
import ca.qc.cvm.dba.persinteret.dao.PersonDAO;
import ca.qc.cvm.dba.persinteret.entity.Person;

public class CorClient extends BaseCorClient  {
	
	public CorClient() {
	}
	
	@Override
	protected void executeTests(final List<String> res, List<String> info) {
		long count = -1;
		try {
			count = PersonDAO.getPeopleCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Nb fiches : " + count);
		
		count = -1;
		try {
			count = PersonDAO.getPhotoCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Nb photos : " + count);
		
		boolean s1 = false;
		try {
			s1 = PersonDAO.deleteAll();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Suppression totale : " + s1);
		
		boolean isFirstLine = true;
		String f1 = "";
		String f2 = "";
		String f3 = "";
		
		for (String line : info) {
			if (line.startsWith("RUN;")) {
				line = line.replace("RUN;", "");
				
				if (isFirstLine) {
					StringTokenizer tokenizer = new StringTokenizer(line, ":");
					f1 = tokenizer.nextElement().toString();
					f2 = tokenizer.nextElement().toString();
					f3 = tokenizer.nextElement().toString();
					isFirstLine = false;
				}
				else {
					StringTokenizer tokenizer = new StringTokenizer(line, ":");
					String p1 = tokenizer.nextElement().toString();
					String p2 = tokenizer.nextElement().toString();
					String p3 = tokenizer.nextElement().toString();
					String p4 = tokenizer.nextElement().toString();
					String p5 = tokenizer.nextElement().toString();
	
					
					byte[] imageInByte = null;
					BufferedImage image;
					try {
						File f = new File(p4);
						image = ImageIO.read(f);
						ByteArrayOutputStream b =new ByteArrayOutputStream();
						ImageIO.write(image, "jpg", b );
						imageInByte = b.toByteArray();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					List<String> p6 = new ArrayList<String>();
					while (tokenizer.hasMoreElements()) {
						p6.add(tokenizer.nextElement().toString());
					}
					
					
					Person p = new Person(p1, p2, p3, p5, p6, imageInByte);
					PersonDAO.save(p);
				}
			}
		}
		
		count = -1;
		try {
			count = PersonDAO.getPeopleCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Nb fiches (ap insertion) : " + count);
		
		count = -1;
		try {
			count = PersonDAO.getPhotoCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Nb photos (ap insertion) : " + count);
		
		try {
			List<Person> p = PersonDAO.getPeopleList("pEx", false, 200);
			if (p.size() > 0) {
				PersonDAO.delete(p.get(p.size() - 1));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		count = -1;
		try {
			count = PersonDAO.getPeopleCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Nb fiches (ap suppression) : " + count);
		
		count = -1;
		try {
			count = PersonDAO.getPhotoCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Nb photos (ap suppression) : " + count);		
		
		String l = "";
		try {
			List<Person> p = PersonDAO.getPeopleList("", true, 200);
			
			for (Person person : p) {
				l += person.toString() + "\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("===== Fiches (avec img) ======\n" + l + "-----------------------------");
		
		l = "";
		try {
			List<Person> p = PersonDAO.getPeopleList("", false, 3);
			
			for (Person person : p) {
				l += person.toString() + "\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("===== Fiches (limite) ======\n" + l + "-----------------------------");

		l = "";
		try {
			List<Person> p = PersonDAO.getPeopleList("", true, 200);
			if (p.size() > 0) {
				Person tmp = p.get(0);
				tmp.setDateOfBirth(f3);
				PersonDAO.save(tmp);
			}
			
			p = PersonDAO.getPeopleList("", false, 200);
			
			for (Person person : p) {
				l += person.toString() + "\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("===== Fiches (après modification " + f3 + ") ======\n" + l + "-----------------------------");
		
		l = "";
		try {
			List<Person> p = PersonDAO.getPeopleList(f1, false, 200);
			
			for (Person person : p) {
				l += person.toString() + "\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("===== Fiches (" + f1 + ") ======\n" + l + "-----------------------------");
		
		l = "";
		try {
			List<Person> p = PersonDAO.getPeopleList(f2, false, 200);
			
			for (Person person : p) {
				l += person.toString() + "\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("===== Fiches (" + f2 + ") ======\n" + l + "-----------------------------");
		
		count = -1;
		try {
			count = PersonDAO.getFreeRatio();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Ratio libre : " + count);
		
		count = -1;
		try {
			count = PersonDAO.getAverageAge();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Âge moyen : " + count);
		
		String r = "";
		try {
			r = PersonDAO.getYoungestPerson();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Plus jeune : " + r);
		
		r = "";
		try {
			r = PersonDAO.getNextTargetName();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Cible : " + r);
		
		s1 = false;
		try {
			s1 = PersonDAO.deleteAll();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Suppression totale : " + s1);

		count = -1;
		try {
			count = PersonDAO.getPeopleCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Nb fiches : " + count);
		
		count = -1;
		try {
			count = PersonDAO.getPhotoCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		res.add("Nb photos : " + count);
		
	}
}

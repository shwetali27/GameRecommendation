package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.stream.JsonReader;

import implimentation.Utility;
import model.RecModel;
import service.RecServiceImpl;

public class Main {
	RecServiceImpl recSerImpl=new RecServiceImpl();

	public static void main(String[] args) {
		
	}
	
	public void onStartUp() {
		FileReader fr;
		String[] entryData;
		String temp = "visitor_id";
		int i;
		try {
			HashMap<String, String> hmap=new HashMap<>();
			JsonReader reader = new JsonReader(new FileReader("abc.json"));
			reader.beginObject();
			while(reader.hasNext()){
				hmap.put(reader.nextName(), reader.nextString());
			}
			
			
			fr = new FileReader("/home/bridgeit/contentDb.csv");
			BufferedReader br = new BufferedReader(fr);
			String entry;
			entry = br.readLine();
			entryData = entry.split("\\,");

			for (i = 0; i < entryData.length; i++) {
				System.out.print(i + " " + entryData[i] + " ");
			}

			while (entry != null) {
				entryData = entry.split("\\,");
				System.out.println(entry);
				for (i = 0; i < entryData.length; i++) {
					entryData[i] = entryData[i].replace("\"", "");
				}

				if (!(entryData[0].equals(temp))) {
					RecModel rm = new RecModel(entryData[0], entryData[1], entryData[2], entryData[3], entryData[4],
							entryData[5],hmap.get(entryData[3]));
					recSerImpl.addVisitor(rm);
				}
				entry = br.readLine();
			}
			System.out.println("Start map");
			recSerImpl.createContentIDMap();
			System.out.println();
			System.out.println("Exit");
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ie) {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void csvRead(String content) {
		System.out.println("Inside controller with contentId = " + content);
		Utility u = new Utility();
		RecModel record = u.fromJson(content);
		
	
	}

}

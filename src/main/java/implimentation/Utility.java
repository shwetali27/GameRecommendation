package implimentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import model.RecModel;

public class Utility {

	Gson gsonObj = new Gson();

	public String toJson(Set<String> contentIDSet) {
		return gsonObj.toJson(contentIDSet);
	}

	public Set<String> toSet(String contentIDString) {
		//System.out.println("Content Id inside utility"+contentIDString);
		return gsonObj.fromJson(contentIDString, new TypeToken<Set<String>>() {
		}.getType());
	}

	public List<String> toList(String contentIDString) {
		return gsonObj.fromJson(contentIDString, new TypeToken<List<String>>() {
		}.getType());
	}
	
	public RecModel toModel(String visitorIDString){
		return gsonObj.fromJson(visitorIDString, RecModel.class);
	}

	public String createContent(String contentName, String categoryName) {
		JsonObject jObj = new JsonObject();
		jObj.addProperty("ContentName", contentName);
		jObj.addProperty("CategoryName", categoryName);
		return jObj.toString();
	}
	
	public RecModel fromJson(String str){
		return gsonObj.fromJson(str, RecModel.class);
	}

	public Properties getProperties() {

		Properties prop = new Properties();
		try {

			String propFileName = "/home/bridgeit/Music/GameRecommendation/resource.properties";
			FileInputStream file = new FileInputStream(getClass().getClassLoader().getResource("resource.properties").getFile());

			// passing propFileName to the FileInputStream class object
			FileInputStream fis;

			fis = new FileInputStream(propFileName);
			if (file != null) {
				// calling load method of Properties class
				prop.load(file);
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}
}

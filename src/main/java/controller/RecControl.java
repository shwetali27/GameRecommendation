package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.stream.JsonReader;

import implimentation.Utility;
import model.RecModel;
import service.RecService;

@RestController
public class RecControl {

	@Autowired
	RecService recServiceImpl;

	@EventListener
	public void onStartUp(ContextRefreshedEvent event) {
		FileReader fr;
		String[] entryData;
		String temp = "visitor_id";
		JsonReader reader;
		int i;
		try {
			HashMap<String, String> hmap=new HashMap<String, String>();
			reader = new JsonReader(new FileReader("/home/bridgeit/Music/GameRecommendation/imageUrl.json"));
			reader.beginObject();
			while(reader.hasNext()){
				//reading the key value pair reading from json and storing the values inside the hash map
				hmap.put(reader.nextName(), reader.nextString());
			}
			fr = new FileReader("/home/bridgeit/Music/GameRecommendation/contentTrial.csv");
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
					recServiceImpl.addVisitor(rm);
					recServiceImpl.addToDao(rm);
				}
				entry = br.readLine();
			}
			System.out.println("Start map");
			recServiceImpl.createContentIDMap();
			System.out.println();
			System.out.println("Exit");
			reader.close();
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
	 //return "redirect:/get";
	}

	@RequestMapping(value = "/getContentID", headers = "Accept=application/json", method = RequestMethod.POST)
	public ModelAndView getSuggestionByJson(@RequestBody String content, RedirectAttributes redirectAttibute) {
		System.out.println("Inside controller with contentId = " + content);
		Utility u = new Utility();
		RecModel record = u.fromJson(content);
		recServiceImpl.addToDao(record);
		redirectAttibute.addFlashAttribute("recmodel", record);
		System.out.println("Outside");
		return new ModelAndView("redirect:/get");
	}
	
	@RequestMapping(value="getContentName",method=RequestMethod.POST)
	public ModelAndView getSuggestionByString(@ModelAttribute("pContentName")String pContentName,RedirectAttributes redirectAttibute){
		System.out.println("Inside controller with contentId = " + pContentName);
		RecModel record = recServiceImpl.getbyContentName(pContentName);
		System.out.println("Inside rec controller: contentid :"+record.getmContentID());
		//recServiceImpl.addToDao(record);
		redirectAttibute.addFlashAttribute("recmodel", record);
		System.out.println("Outside");
		return new ModelAndView("redirect:/getContent");
	}

}
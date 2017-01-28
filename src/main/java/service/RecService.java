package service;

import java.util.List;
import java.util.Set;

import model.RecModel;

public interface RecService {
	//public RecModel getEntry(String contentId);
	
	public void addVisitor(RecModel rm);
	
	public void createContentIDMap();
	
	public Set<String> getSuggestion(RecModel rm);
	
	public void addToContentMap(RecModel rm);
	
	public void addToDao(RecModel rm);
	
	public RecModel getbyContentID(String pContentID);

	public RecModel getbyContentName(String pContentName);
	
	public List<String> getSuggestion(String pContentName);

}

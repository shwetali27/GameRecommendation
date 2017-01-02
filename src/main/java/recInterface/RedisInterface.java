package recInterface;

import java.util.Set;

import model.RecModel;

public interface RedisInterface {

	public void addVisitorView(RecModel rm);
	
	public void addVisitorDownload(RecModel rm);
	
	public void addContentID(RecModel rm);
	
	public void createContentIDMap();
	
	public void addToContentMap(RecModel rm);
	
	public Set<String> getSuggestion(String contentID, String visitorID);
}
